angular.module('newApp')
.controller('myCalendarCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {
	  //$.fn.Data.checkbox();
	$scope.eventList = [];
	$scope.flag = 0;
	$scope.initfunction = function(){
		console.log("ediiii");
		$http.get('/getTimeTableOfPhotos').success(function(data) {
			/*rendering:'background',*/
			console.log(data);
			$scope.postalNameList = data.postalNameList;
			$scope.calenderTimeTable = data.calenderTimeTable;
			angular.forEach(data.calenderTimeTable, function(obj, index){
				$scope.eventList.push({
					id:obj.id,
					title:obj.portalName,
					name:obj.portalName,
					start: obj.contractDurStartDate+"T"+obj.openTime,
					end: obj.contractDurStartDate+"T"+obj.closeTime,
					backgroundColor:obj.setColor,
					locationId:obj.locationId,
					editable:true,
					allDay: false,
					
	   	  			
				});
			});
			
			angular.forEach($scope.postalNameList, function(obj, index){
				$('#external-events').append("<div class='external-event col-md-12' style='color: white;background:"+obj.color+";'>"+obj.title+"</div>");
			});
			
			$('.external-event').addClass('fc-event-draggable');
		
			console.log($scope.eventList);
			
				$scope.initFirstFunction($scope.eventList);
		});
	}
	
	
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
	            revertDuration: 0,  //  original position after the drag
	            backgroundColor:"red"
	        });
	    };


	    $('#external-events div.external-event').each(function() {
	    	
	        eventDrag($(this));
	    });
		
			$('#calendar').fullCalendar({
				  
			    defaultView: 'agendaWeek',
			    editable: true,
			    droppable: true,
			    events: eventList,
		        /*eventRender: function(event, element) {
		        	console.log(event);
		        	angular.forEach($scope.eventList, function(obj, index){
		        		if(obj.id == event.id){
		        			console.log("DDD000");
		        		}
					});
		        },*/
		        eventClick: function(event, jsEvent, view) {
		        	console.log(event);
		        	console.log(jsEvent);
		        	console.log(view);
		        	//$('#calendar').fullCalendar('removeEvents',event._id);

			    },
			    
			   
		        eventDrop: function (event, delta, revertFunc, jsEvent, ui, view) {
		        	 $scope.flagSave = 0;
		        	$scope.saveObj = {};
		            
		            $scope.date = 0;
		           
		            if(event._start._i instanceof Object == true){
		            	$scope.saveObj.originStartDate = "0";
		            }else{
		            	$scope.saveObj.originStartDate = event._start._i;
		            }
		            	$scope.saveObj.portalName = event.title;
		            	$scope.saveObj.contractDurStartDate = event._start._d;
		            	if(event._end == null){
		            		$scope.saveObj.contractDurEndDate = "0";
		            		$scope.saveObj.originEndDate = "0";
		            	}else{
		            		$scope.saveObj.contractDurEndDate = event._end._d;
		            		$scope.saveObj.originEndDate = event._end._i;
		            	}
		            	//console.log($filter('date')(delta._milliseconds, 'hh:mm:ss'));
		            	$scope.saveObj.openTime = delta._milliseconds;
		            	
		            	$http.post("/saveNewEvent",$scope.saveObj).success(function(data){
		       			 	console.log("suuu888");
		       			 	$scope.initfunction();
		       			 	//$("#calendar").fullCalendar("refetchEvents");
		            	});
		            
		           
		        },
		        eventResize: function (event, delta, revertFunc, jsEvent, ui, view) {
		        	
		            $scope.saveObj = {};
		            
		          /*  angular.forEach($scope.eventList, function(obj, index){
		            	if(obj.id == event.id){
		            		
		            		var arr = [];
		        			arr = obj.end.split('T');
		            		$scope.date = arr[0];
		            		var arrTime = [];
		            		arrTime = arr[1].split(':');
		            		var secTomill = arrTime[2] * 1000;
		            		var minTomill = arrTime[1] * 60000;
		            		var hoToMin = arrTime[0] * 60;
		            		var houTomill = hoToMin * 60000;
		            		
		            		var totalMill = secTomill + minTomill + houTomill;
		            		console.log(totalMill);
		            		date = new Date($scope.date);
		            		
		            		var d = new Date(arr[1]);
		            		var n = d.getMilliseconds();
		            		
				             date.setDate(date.getDate() + delta._days);
				            console.log("000099999900000");
				            console.log(arr[1]);
				            console.log(n);
				            console.log(n+delta._milliseconds);
				            console.log($scope.date);
				            console.log(date);
		            	}
		            });*/
		            
		          /*  $scope.date = 0;
		            angular.forEach(event.source.events, function(obj, index){
		            	if(obj.id == event.id){
		            		console.log("ddd1122");
		            		$scope.date = obj.contractDurStartDate;
		            	}
		            });*/
		            
		            /*$http.get('/getEditEventResize/'+delta._milliseconds+"/"+delta._days+"/"+delta._months+"/"+event.id+"/"+$scope.date).success(function(data) {
		            	console.log("susccccccccccc");
		            	//$scope.initfunction();
		            });*/
		            
		            
		            /*if(event._start._i instanceof Object == true){
		            	$scope.saveObj.originStartDate = "0";
		            }else{
		            	if(event._start == null){
		            		$scope.saveObj.originStartDate = "0";
		            	}else{
		            		$scope.saveObj.originStartDate = event._start._i;
		            	}
		            	
		            }*/
		            $scope.saveObj.originStartDate = "0";
		            	$scope.saveObj.portalName = event.title;
		            	$scope.saveObj.contractDurStartDate = event._start._d;
		            	if(event._end == null){
		            		$scope.saveObj.contractDurEndDate = "0";
		            		$scope.saveObj.originEndDate = "0";
		            	}else{
		            		$scope.saveObj.contractDurEndDate = event._end._d;
		            		$scope.saveObj.originEndDate = event._end._i;
		            	}
		            	//console.log($filter('date')(delta._milliseconds, 'hh:mm:ss'));
		            	$scope.saveObj.openTime = delta._milliseconds;
		            	
		            	$http.post("/saveNewEvent",$scope.saveObj).success(function(data){
		       			 	console.log("suuu888");
		            	});
		            
		        },
		        eventRender: function(event, element) {
		        	console.log(event);
		        	angular.forEach($scope.eventList, function(obj, index){
		        		if(obj.id == event.id){
		        			console.log("DDD000");
		        		}
					});
		        },
		        disableResizing: true,
		        // this allows things to be dropped onto the calendar !!!
		        drop:function(date, allDay) { // this function is called when something is dropped
		        	console.log(date);
		            // retrieve the dropped element's stored Event Object
		            var originalEventObject = $(this).data('eventObject');
		          
		            // we need to copy it, so that multiple events don't have a reference to the same object
		            var copiedEventObject = $.extend({}, originalEventObject);
		            console.log(copiedEventObject);
		            angular.forEach($scope.eventList, function(obj, index){
		        		if(obj.title == copiedEventObject.title){
		        			copiedEventObject.backgroundColor = obj.backgroundColor;
		        		}
					});
		            
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
			
			
		    /* eventClick: function(calEvent, jsEvent, view) {
		        	console.log(calEvent);
		            var title = prompt('Event Title:', calEvent.title, { buttons: { Ok: true, Cancel: false} });

		            if (title){
		                calEvent.title = title;
		                calendar.fullCalendar('updateEvent',calEvent);
		            }
		        },*/
			/*angular.forEach($scope.eventList, function(obj, index){
				$('#'+obj.id+'.fc-event').each(function() {

			        // create an Event Object (http://arshaw.com/fullcalendar/docs/event_data/Event_Object/)
			        // it doesn't need to have a start or end
			        var eventObject = {
			            title: $.trim($(this).text()) // use the element's text as the event title
			        };
			        console.log(eventObject);
			        // store the Event Object in the DOM element so we can get to it later
			        $(this).data('eventObject', eventObject);

			        // make the event draggable using jQuery UI
			        $(this).draggable({
			            zIndex: 999,
			            revert: true, // will cause the event to go back to its
			            revertDuration: 0 //  original position after the drag
			        });
			    });
			});*/
			
		
			
			
			
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
