package org.nidhishah.meetingscheduler.controller;

import org.nidhishah.meetingscheduler.dto.NewOrgMemberDTO;
import org.nidhishah.meetingscheduler.dto.TeamMemberDTO;
import org.nidhishah.meetingscheduler.security.UserPrincipal;
import org.nidhishah.meetingscheduler.services.ClientServiceImpl;
import org.nidhishah.meetingscheduler.services.TeamMemberServiceImpl;
import org.nidhishah.meetingscheduler.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ClientController {

    private UserServiceImpl userService;
    private ClientServiceImpl clientService;
    private TeamMemberServiceImpl teamMemberService;
    @Autowired
    public ClientController(UserServiceImpl userService, ClientServiceImpl clientService
                            ,TeamMemberServiceImpl teamMemberService) {
        this.userService = userService;
        this.clientService = clientService;
        this.teamMemberService = teamMemberService;
    }

    @PostMapping("/addclient")
    public String addNewClient(Model model, @ModelAttribute("newmember") NewOrgMemberDTO newOrgMemberDTO,
                                @RequestParam String roleName, RedirectAttributes redirectAttributes) {
        try {
            System.out.println("Inside the add client");
            System.out.println(newOrgMemberDTO.getEmail());
            System.out.println(newOrgMemberDTO.getFirstName());
            System.out.println(newOrgMemberDTO.getLastName());
            System.out.println(roleName); // roleName will be "client" here
            // get authenticated admin organization
            //check the client is already with organization or not. either as client, teammember or as admin
            // if is there, send error, this client is already exist with your organization
            // else: save this person and generate one time code and send email to the person and refresh the list in admin page
            //return the admin page.
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
                //organization information collection
                UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
                String adminOrganization = userPrincipal.getOrganizationName();
                //new member found
                if(userService.findUserByEmailAndOrganization(newOrgMemberDTO.getEmail(), adminOrganization)){
                    redirectAttributes.addFlashAttribute("clientAddError",newOrgMemberDTO.getEmail() + " is already member of "+ adminOrganization);
                    return "redirect:/adm_dashboard";
                }
                else{
                    //new member not found, so add to organization
                    clientService.registerNewClient(newOrgMemberDTO,adminOrganization);
                    redirectAttributes.addFlashAttribute("clientAddSuccess","New client added successfully and email sent.");
                    return "redirect:/adm_dashboard";

                }
                //Find that the new member is present in that organization or not

            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return "file";
    }

    /**
     * This form only gets all team members first name and last name who are available for meetings..
     */

    @GetMapping("meeting_schedule")
    public String getMeetingScheduleForm1(Model model){
        try{
            //get authenticated person info
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
                //organization information collection
                UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
                String organizationName = userPrincipal.getOrganizationName();
                System.out.println("Client page get form1 controller");
                //call teammember service to get all teammembers list who are available for meetings
                List<TeamMemberDTO> availableTeamMembers= teamMemberService.getAvailableTeamMembersForMeetingSchedule(organizationName);
                System.out.println("Received "+availableTeamMembers.size() +" available from back for " + organizationName);
                model.addAttribute("teamembers",availableTeamMembers);
                model.addAttribute("organization",organizationName);
                return "client_page";
            }
            else{
                throw new Exception();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            return "login";
        }
    }

}
