document.addEventListener("DOMContentLoaded", function () {
    // Get the timezone select element
    const timezoneSelect = document.getElementById("timezone");

    // Get the list of timezones and populate the dropdown
    moment.tz.names().forEach(function (timezone) {
        const option = document.createElement("option");
        option.value = timezone;
        option.text = timezone + ' - ' + moment.tz(timezone).format('z (Z)');
        timezoneSelect.appendChild(option);
    });

    // Detect the user's browser timezone and set it as the default selected option
    const browserTimezone = moment.tz.guess();
    timezoneSelect.value = browserTimezone;

    // Get the Zoom checkbox and Zoom Link input elements
    const zoomCheckbox = document.getElementById("zoom");
    const zoomLinkDiv = document.getElementById("zoomLinkDiv");

    // Add an event listener to the Zoom checkbox
    zoomCheckbox.addEventListener("change", function () {
        if (zoomCheckbox.checked) {
            // If the Zoom checkbox is checked, show the Zoom Link input
            zoomLinkDiv.style.display = "block";
        } else {
            // If the Zoom checkbox is unchecked, hide the Zoom Link input
            zoomLinkDiv.style.display = "none";
        }
    });
});

//
//     $(document).ready(function () {
//     // Add a common class to all the '+' buttons
//     $('.addTimeSlotBtn').click(function () {
//         // Identify the specific day container
//         var dayContainer = $(this).closest('.day-container');
//         var id = $(this).closest('.day-content')
//         console.log(id);
//         console.log(id.id);
//         console.log(id.index())
//
//         // Clone the last time slot div within the specific day container
//         var lastSlot = dayContainer.find('.card-body:last');
//         var newSlot = lastSlot.clone();
//
//         // Update the index in the cloned div
//         var newIndex = parseInt(newSlot.find('[th:each]').attr('th:each').match(/\d+/)[0]) + 1;
//         newSlot.find('[th:each]').attr('th:each', 'timeSlot, dayIndex : ${daysavail.' + $(this).data('day') + 'TimeSlot[' + newIndex + ']}');
//
//         // Clear values in the cloned div
//         newSlot.find('input').val('');
//
//         // Append the cloned div to the day container
//         dayContainer.find('.day-content').append(newSlot);
//     });
// });
$(document).ready(function () {
    // Handle the button click
    $('.addTimeSlotBtn').on('click', function () {
        // Find the closest card-body element
        var cardBody = $(this).closest('.card').find('.card-body');

        // Get the index for the new row
        var index = $(this).closest('.day-container').find('.day-content').length;
        console.log(index);

        // Get the day from data attribute of the first element with the class 'day-content'
        var day = $(this).closest('.day-container').find('.day-content:first').data('day-section');
        console.log(day);

        // Create a new row div using jQuery
        var newRow = $('<div class="row d-flex day-content" data-day-section="' + day + '"></div>');

        // Append the columns to the new row
        newRow.append('<div class="col-md-6">' +
            '<div class="input-group date bootstrap-timepicker">' +
            '<input type="text" class="form-control timePicker" name="mondayTimeSlot[' + index + '].startTime">' +
            '<span class="input-group-addon">' +
            '<span class="input-group-text bg-white">' +
            '<i class="bi bi-clock" aria-hidden="true"></i>' +
            '</span>' +
            '</span>' +
            '</div>' +
            '</div>');

        newRow.append('<div class="col-md-6">' +
            '<div class="input-group date bootstrap-timepicker" id="monday-end-time-' + index + '">' +
            '<input type="text" class="form-control timePicker" name="mondayTimeSlot[' + index + '].endTime">' +
            '<span class="input-group-addon">' +
            '<span class="input-group-text bg-white">' +
            '<i class="bi bi-clock" aria-hidden="true"></i>' +
            '</span>' +
            '</span>' +
            '</div>' +
            '</div>');

        // Append the new row under the card-body
        cardBody.append(newRow);

        // Reinitialize the Timepicker for the new row
        newRow.find('.timePicker').timepicker({
            showInputs: false,
            showMeridian: false
        });
    });
});



