angular.module('newApp')
.controller('myCalendarCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout) {
	  //$.fn.Data.checkbox();
	$scope.eventList = [];
	$scope.flag = 0;
	$scope.initfunction = function(){
		$http.get('/getTimeTableOfPhotos').success(function(data) {
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
			console.log(el);
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
			    
//		        eventClick: function(event, jsEvent, view) {
//		        	//console.log(event);
//		        	//console.log(jsEvent);
//		        	//console.log(view);
//		        	$scope.saveObj = {};
//		        	
//		        	
//			            	/*$scope.saveObj.portalName = event.title;
//			            	$scope.saveObj.contractDurStartDate = event._start._d;
//			            	if(event._end == null){
//			            		$scope.saveObj.contractDurEndDate = "0";
//			            		$scope.saveObj.originEndDate = "0";
//			            	}else{
//			            		$scope.saveObj.contractDurEndDate = event._end._d;
//			            		$scope.saveObj.originEndDate = event._end._i;
//			            	}
//			            	console.log($scope.saveObj);
//			            	
//			            	$http.post("/deleteEvent",$scope.saveObj).success(function(data){
//			       			 	console.log("suuu888");
//			       			 	$('#calendar').fullCalendar('removeEvents',event._id);
//			            	});*/
//
//			    },
			    			    
			   
		        eventDrop: function (event, delta, revertFunc, jsEvent, ui, view) {
		        	 $scope.flagSave = 0;
		        	$scope.saveObj = {};
		            var flagDataAb = 0;
		            $scope.date = 0;
		            angular.forEach($scope.eventList, function(obj, index){
		            	console.log(obj);
		            	console.log(event);
		        		if(obj.title == event.title){
		        			
		        			var arr = [];
		        			arr = obj.start.split('T');
		        			console.log(arr[0]);
		        			console.log($filter('date')(event._start._d, 'yyyy-MM-dd'));
		        			if(arr[0] == $filter('date')(event._start._d, 'yyyy-MM-dd')){
		        				
		        				if(obj.id != event.id){
		        					flagDataAb = 1;
		        				}
		        			}
		        		}
					});
		            if(flagDataAb == 0){
		            	
		           
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
			       			 //	$scope.initfunction();
			            	});
		            
		            }else{
		            	$('#calendar').fullCalendar('removeEvents',event.id);
		            	$('#eventExist').modal('show');
		            }
		        },
		        
		        
		        eventResize: function (event, delta, revertFunc, jsEvent, ui, view) {
		        	
		            $scope.saveObj = {};
		        
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
		            	$scope.saveObj.openTime = delta._milliseconds;
		            	
		            	$http.post("/saveNewEvent",$scope.saveObj).success(function(data){
		       			 	console.log("suuu888");
		            	});
		            
		        },
		        eventRender: function(event, element) {
		        	 //element.append( "<span ng-click='deleteActivityModal()'>X</span>" );
		        	element.bind('mousedown', function (e) {
		        		console.log(e);
		        		console.log(event);
		                if (e.which == 3) {
		                	$scope.eventdata = event;
		                	$('#deleteForeverModal').modal('show');
		                }
		            });
                
		        },
		        disableResizing: true,
		        // this allows things to be dropped onto the calendar !!!
		        drop:function(date, allDay) { // this function is called when something is dropped
		        	console.log(date);
		        	console.log(allDay);
		        	$scope.saveObj = {};
		            // retrieve the dropped element's stored Event Object
		            var originalEventObject = $(this).data('eventObject');
		            // we need to copy it, so that multiple events don't have a reference to the same object
		            var copiedEventObject = $.extend({}, originalEventObject);
		            console.log(copiedEventObject);
		            console.log($filter('date')(date._d, 'yyyy-MM-dd'));
		            var flagDataAb = 0;
		            angular.forEach($scope.eventList, function(obj, index){
		            	console.log(obj.backgroundColor);
		        		if(obj.title == copiedEventObject.title){
		        			var arr = [];
		        			arr = obj.start.split('T');
		        			if(arr[0] == $filter('date')(date._d, 'yyyy-MM-dd')){
		        				flagDataAb = 1;
		        			}
		        			copiedEventObject.backgroundColor = obj.backgroundColor;
		        		}
					});
		            if(flagDataAb == 0){
		            	// assign it the date that was reported
			            copiedEventObject.start = date;
			            copiedEventObject.allDay = false;

			            // render the event on the calendar
			            // the last `true` argument determines if the event "sticks" (http://arshaw.com/fullcalendar/docs/event_rendering/renderEvent/)
			            $('#calendar').fullCalendar('renderEvent', copiedEventObject, true);
			            
			            
			            
			           
			            	$scope.saveObj.portalName = copiedEventObject.title;
			            	$scope.saveObj.contractDurStartDate = date._d;
			            	$scope.saveObj.originStartDate = "0";
			            	$scope.saveObj.contractDurEndDate = "0";
			            	$http.post("/saveNewEvent",$scope.saveObj).success(function(data){
			       			 	console.log("suuu888");
			       			 //	$scope.initfunction();
			            	});
			            
			            
			            
		            }else{
		            	$('#eventExist').modal('show');
		            }
		            
		           
		            // is the "remove after drop" checkbox checked?
		            if ($('#drop-remove').is(':checked')) {
		                // if so, remove the element from the "Draggable Events" list
		                $(this).remove();
		            }

		        }
			  
			});

	}
	
	$scope.deleteEvent = function(){
		console.log("sddsd");
		console.log($scope.eventdata);
		
		$scope.saveObj = {};
    	
    	
    	$scope.saveObj.portalName = $scope.eventdata.title;
    	$scope.saveObj.contractDurStartDate = $scope.eventdata._start._d;
    	if($scope.eventdata._end == null){
    		$scope.saveObj.contractDurEndDate = "0";
    		$scope.saveObj.originEndDate = "0";
    	}else{
    		$scope.saveObj.contractDurEndDate = $scope.eventdata._end._d;
    		$scope.saveObj.originEndDate = $scope.eventdata._end._i;
    	}
    	console.log($scope.saveObj);
    	
    	$http.post("/deleteEvent",$scope.saveObj).success(function(data){
			 	console.log("suuu888");
			 	$('#calendar').fullCalendar('removeEvents',$scope.eventdata._id);
    	});
	}
	 
	
}]);	
