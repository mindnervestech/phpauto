angular.module('newApp')
.controller('myCalendarCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {
	  //$.fn.Data.checkbox();
	$scope.eventList = [];
	$scope.flag = 0;
	
	$http.get('/getTimeTableOfPhotos').success(function(data) {
		
		console.log(data);
		$scope.postalNameList = data.postalNameList;
		angular.forEach(data.calenderTimeTable, function(obj, index){
			$scope.eventList.push({
				title:obj.portalName,
				name:obj.portalName,
				start: obj.contractDurStartDate+"T"+obj.openTime,
				end: obj.contractDurStartDate+"T"+obj.closeTime,
				rendering:'background',
				description:"uuuu",
				color:obj.setColor,
   	  			
			});
		});
	
		console.log($scope.eventList);
		
		//$timeout(function() {
			$scope.initFirstFunction($scope.eventList);
		  // }, 10000);
	});
	
	$scope.initFirstFunction = function(eventList){
		$scope.flag = 0;
		console.log(eventList);
		console.log(eventList.length);
		/*header: {
            left: 'prev,next today',
            center: 'title',
            right: 'agendaWeek,agendaDay'
        },*/
		
		var eventDrag = function(el){
	        // create an Event Object (http://arshaw.com/fullcalendar/docs/event_data/Event_Object/)
	        // it doesn't need to have a start or end
	        var eventObject = {
	            title: $.trim(el.text()) // use the element's text as the event title
	        };

	        // store the Event Object in the DOM element so we can get to it later
	        el.data('eventObject', eventObject);

	        // make the event draggable using jQuery UI
	        el.draggable({
	            zIndex: 999,
	            revert: true,      // will cause the event to go back to its
	            revertDuration: 0  //  original position after the drag
	        });
	    };


	    $('#external-events div.external-event').each(function() {
	        eventDrag($(this));
	    });
		
			$('#calendar').fullCalendar({
				
			    defaultView: 'agendaWeek',
			    events: eventList,
			    editable: true,
		        droppable: true,
		        // this allows things to be dropped onto the calendar !!!
		        drop:function(date, allDay) { // this function is called when something is dropped

		            // retrieve the dropped element's stored Event Object
		            var originalEventObject = $(this).data('eventObject');

		            // we need to copy it, so that multiple events don't have a reference to the same object
		            var copiedEventObject = $.extend({}, originalEventObject);

		            // assign it the date that was reported
		            copiedEventObject.start = date;
		            copiedEventObject.allDay = allDay;

		            // render the event on the calendar
		            // the last `true` argument determines if the event "sticks" (http://arshaw.com/fullcalendar/docs/event_rendering/renderEvent/)
		            $('#calendar').fullCalendar('renderEvent', copiedEventObject, true);

		            // is the "remove after drop" checkbox checked?
		            if ($('#drop-remove').is(':checked')) {
		                // if so, remove the element from the "Draggable Events" list
		                $(this).remove();
		            }

		        }
			
			});
			
			
			
			 /* var addEvent = function (name) {
				  console.log("gyyyyyyuuu");
			        name = name.length === 0 ? "Untitled Event" : name;
			        var html = $('<div class="external-event label label-default">' + name + '</div>');
			        $('#event-box').append(html);
			        eventDrag(html);
			    };*/

			    /*$('#event-add').on('click', function () {
			        var name = $('#event-name').val();
			        addEvent(name);
			    });

			    $(".page-content").css("padding-bottom", "120px");*/
		
		//var element1 = angular.element("<div id='event-box'><div class='external-event col-md-12' style='color: white;background:red;'>kkkk</div></div>");
		  //$("#showDiv").append(element1);
			
			

	}
	

	
	
	
	
	
	

	    

	    /* initialize the calendar
	     -----------------------------------------------------------------*/
	    
	    /*$('#calendar').fullCalendar({
	        header: {
	            left: 'prev,next today',
	            center: 'title',
	            right: 'month,agendaWeek,agendaDay'
	        },
	        editable: true,
	        droppable: true, // this allows things to be dropped onto the calendar !!!
	        drop: function(date, allDay) { // this function is called when something is dropped

	            // retrieve the dropped element's stored Event Object
	            var originalEventObject = $(this).data('eventObject');

	            // we need to copy it, so that multiple events don't have a reference to the same object
	            var copiedEventObject = $.extend({}, originalEventObject);

	            // assign it the date that was reported
	            copiedEventObject.start = date;
	            copiedEventObject.allDay = allDay;

	            // render the event on the calendar
	            // the last `true` argument determines if the event "sticks" (http://arshaw.com/fullcalendar/docs/event_rendering/renderEvent/)
	            $('#calendar').fullCalendar('renderEvent', copiedEventObject, true);

	            // is the "remove after drop" checkbox checked?
	            if ($('#drop-remove').is(':checked')) {
	                // if so, remove the element from the "Draggable Events" list
	                $(this).remove();
	            }

	        }
	    });
*/
	  /*  var addEvent = function (name) {
	        name = name.length === 0 ? "Untitled Event" : name;
	        var html = $('<div class="external-event label label-default">' + name + '</div>');
	        $('#event-box').append(html);
	        eventDrag(html);
	    };

	    $('#event-add').on('click', function () {
	        var name = $('#event-name').val();
	        addEvent(name);
	    });

	    $(".page-content").css("padding-bottom", "120px");*/
	
	
}]);	
