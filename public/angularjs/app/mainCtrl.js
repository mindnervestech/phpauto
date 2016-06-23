angular.module('newApp').controller('mainCtrl',
    ['$scope', 'applicationService', 'quickViewService', 'builderService', 'pluginsService', '$location','$http','$filter','$interval','$rootScope',
        function ($scope, applicationService, quickViewService, builderService, pluginsService, $location,$http,$filter,$interval,$rootScope) {
    	var ele = document.getElementById('loadingmanual');	
    	$(ele).hide();
            $(document).ready(function () {
                applicationService.init();
                quickViewService.init();
                builderService.init();
                pluginsService.init();
                Dropzone.autoDiscover = false;
            });
            
            var array = [];
            $scope.userRole=userRole;
            console.log("**userRole"+userRole);
            $http.get('/getUserInfo').success(function(data,status, headers, config){
            	
            	$scope.name = data.firstName + " " + data.lastName;
            	/*if(data.imageName == null){
            		$scope.userimage = data.imageUrl;
            	}else{*/
            		$scope.userimage = "http://glider-autos.com/glivrImg/images"+data.imageUrl;
            		
            	/*}*/

        		console.log($scope.userimage);
            	
            }).error(function(data,status){
            	if(status == 401) {
            		window.location.href = "/login";
            	}
            });
            
            
            
            $scope.leadData={};
            $http.get('/getLeadInfo').success(function(data,status, headers, config){
            
            	console.log("*****");
            	console.log(data);
            	$scope.leadData=data;
            	$scope.leadData = $filter('orderBy')($scope.leadData,'timeDiff');
            	//$scope.leadData = $scope.leadData.reverse();
            	$scope.dataLength=$scope.leadData.length;
               
            })
            
            $http.get('/getSalesUserValue')
		.success(function(data){
			console.log(data);
			$scope.salesPersonPerf = data;
			
		    });
            
            $scope.assignCanceledLead = function(item) {
        		console.log(item.typeOfLead);
              	$scope.leadlId = item.id;
              	$scope.leadType = item.typeOfLead;
              	$scope.changedUser = "";
              	$('#btnAssig').click();
              }
              
            
            $scope.setNewFlag = function() {
            	console.log("inside.....in")
                $scope.flagForPop=0;
              }
            
            $scope.flagForPop=0;
            $scope.setFlagForPop = function() {
            	
              $scope.flagForPop=1;
            }
            
            $scope.setNewFlagNew = function() {
            	console.log("inside.....in")
                $scope.flagForPopNew=0;
              }
            
            $scope.flagForPopNew=0;
            $scope.setFlagForPopNew = function() {
            	
              $scope.flagForPopNew=1;
            }
            
            $scope.changeUser = function() {
            console.log("inside");	
   			 console.log($scope.leadlId);
   			 console.log($scope.leadType);
   			 console.log($scope.changedUser);
   	        	$http.get('/changeAssignedUser/'+$scope.leadlId+'/'+$scope.changedUser+'/'+$scope.leadType)
   				.success(function(data) {
   					$('#assignUserModal').modal('hide');
   					$.pnotify({
   					    title: "Success",
   					    type:'success',
   					    text: "User assigned successfully",
   					});
   				});
   	        	
   	        }
            
            $scope.releaseLeads = function(entity){
            	console.log(entity.type);
   			$http.get('/releaseLeads/'+entity.id+'/'+entity.type)
   				.success(function(data) {
   					$.pnotify({
   					    title: "Success",
   					    type:'success',
   					    text: "This lead has been released for everyone to claim ",
   					});
   				});
   		 }
            
            
            $scope.setAsRead = function (item) {
                console.log("IIIIDDDD");
                
                console.log(item);
                var flag=true;
                if(item.typeOfLead =="Request More"){
                	 $http.get('/requestInfoMarkRead/'+flag+'/'+item.id)
             		.success(function(data) {
             			$.pnotify({
						    title: "Success",
						    type:'success',
						    text: "Claimed Successfully",
						});
             		console.log(data);
             	});
                	
                }
                
                else if(item.typeOfLead =="Schedule Test"){
                	var flag=true;
                	$http.get('/scheduleTestMarkRead/'+flag+'/'+item.id)
            		.success(function(data) {
            			$.pnotify({
						    title: "Success",
						    type:'success',
						    text: "Claimed Successfully",
						});

            			console.log(data);
            		});
                }
                
               else if(item.typeOfLead =="Trade In"){
            	   
            	   $http.get('/tradeInMarkRead/'+flag+'/'+item.id)
       			.success(function(data) {
       				$.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Claimed Successfully",
					});
       				console.log(data);
       	       	});
                }
               else if(item.typeOfLead =="Premium"){
            	   $http.get('/changeAssignedUser/'+item.id+'/'+userKey+'/'+item.type)
					.success(function(data) {
						$.pnotify({
						    title: "Success",
						    type:'success',
						    text: "Claimed Successfully",
						});
					});
            	   
               	
               }
                
                
            };


            $http.get('/getInfoCount').success(function(data,status, headers, config){
            	
            	$scope.requestMoreLength = data.req;
                $scope.scheduleTestLength = data.schedule;
                $scope.tradeInLength = data.trade;
                $scope.premiumlength = data.premium;
                $scope.userType = data.userType;
                
                $scope.setFalg = 0;
                
                if($scope.userType == "General Manager"){
                	 if(locationId != 0){
                     	$scope.setFalg = 1;
                     	$http.get('/getLocationName/'+locationId)
            			.success(function(data1) {
            				
            				 $scope.locationName=data1;
            				 console.log($scope.locationName);
            				 
            			});	
                    
                     }
                }else{
                	$scope.setFalg = 1;
                	$scope.locationName=data.locationName;
                	console.log("::locationname");
                	console.log($scope.locationName);
                }
               
            })
            
            var promo =  $interval(function(){
				
				$http.get('/getInfoCount').success(function(data,status, headers, config){
	            	$scope.requestMoreLength = data.req;
	                $scope.scheduleTestLength = data.schedule;
	                $scope.tradeInLength = data.trade;
	                $scope.premiumlength = data.premium;
	                $scope.userType = data.userType;
	            })
				},15000);
            
            $scope.$on('getCountEvent', function (event, args) {
            	$http.get('/getInfoCount').success(function(data,status, headers, config){
                	$scope.requestMoreLength = data.req;
                    $scope.scheduleTestLength = data.schedule;
                    $scope.tradeInLength = data.trade;
                    $scope.premiumlength = data.premium;
                    $scope.userType = data.userType;
                })
            });
            
            
            $scope.$on('$viewContentLoaded', function () {
                pluginsService.init();
                applicationService.customScroll();
                applicationService.handlePanelAction();
                $('.nav.nav-sidebar .nav-active').removeClass('nav-active active');
                $('.nav.nav-sidebar .active:not(.nav-parent)').closest('.nav-parent').addClass('nav-active active');

                if($location.$$path == '/' || $location.$$path == '/layout-api'){
                    $('.nav.nav-sidebar .nav-parent').removeClass('nav-active active');
                    $('.nav.nav-sidebar .nav-parent .children').removeClass('nav-active active');
                    if ($('body').hasClass('sidebar-collapsed') && !$('body').hasClass('sidebar-hover')) return;
                    if ($('body').hasClass('submenu-hover')) return;
                    $('.nav.nav-sidebar .nav-parent .children').slideUp(200);
                    $('.nav-sidebar .arrow').removeClass('active');
                }

            });

            $scope.isActive = function (viewLocation) {
                return viewLocation === $location.path();
            };
            
            
            
            $scope.showDetailsFunction = function(infoNotifiction){
            	$scope.arrayAdd = [];
            	console.log(infoNotifiction);
            	if(infoNotifiction.findBy == "month plan"){
            		$scope.planForsalePersonForMonth(infoNotifiction.value.month);
            	}
            	if(infoNotifiction.findBy == "declined meeting"){
            		$scope.arrayAdd.push(infoNotifiction.value);
            		$scope.decline($scope.arrayAdd);
            	}
            	if(infoNotifiction.findBy == "comment like"){
            		$scope.arrayAdd.push(infoNotifiction.value);
            		$scope.likeMsg($scope.arrayAdd);
            	}
            	if(infoNotifiction.findBy == "invitation received"){
            		$scope.arrayAdd.push(infoNotifiction.value);
            		$scope.invitationMsg($scope.arrayAdd);
            	}
            	if(infoNotifiction.findBy == "coming soon"){
            		$scope.arrayAdd.push(infoNotifiction.value);
            		$rootScope.$emit("CallComingSoonMethod", {});
            	}
            	if(infoNotifiction.findBy == "accept meeting"){
            		$scope.arrayAdd.push(infoNotifiction.value);
            		$scope.acceptMsg($scope.arrayAdd);
            	}
            	if(infoNotifiction.findBy == "delete meeting"){
            		$scope.arrayAdd.push(infoNotifiction.value);
            		$scope.deleteMeeting($scope.arrayAdd);
            	}
            	if(infoNotifiction.findBy == "update meeting"){
            		$scope.arrayAdd.push(infoNotifiction.value);
            		$scope.updateMeeting($scope.arrayAdd);
            	}
            	if(infoNotifiction.findBy == "reminder popup"){
            		$scope.arrayAdd.push(infoNotifiction.value);
            		$rootScope.$emit("CallReminderMethod", {});
            		
            	}
            	
            
            }
            
            $scope.dismissFunction = function(infoNotifiction){
            	angular.forEach($scope.notificationArray, function(value, key) {
            		if(infoNotifiction.findBy == value.findBy){
            			$scope.notificationArray.splice(key, 1);
            		}
            	});
      }
            
            $scope.notificationArray = [];
            $scope.notifictionCount = 0;
            $scope.indexInitFunction = function(){
            	
            	 $scope.notificationArray = [];
            	$http.get('/getNotificationData').success(function(data,status, headers, config){

                	angular.forEach(data.commentLike, function(value, key) {
                		$scope.notificationArray.push({
							title: value.firstName+"  "+ value.lastName+" just Liked your work!"+ value.userComment,
							iconClass: "glyphicon glyphicon-heart editClassColorRed",
							findBy:"comment like",
							value:value,
						});
                		$scope.notifictionCount++;
                	});
                	
                	angular.forEach(data.planScheduleMonthly, function(value, key) {
                		var month=value.month;
    					if(month !=null){
    						month = month.toLowerCase();
    						month=month.substring(0,1).toUpperCase()+month.substring(1);
    					}
                		
                		$scope.notificationArray.push({
							title: month+"'s plan has been assigned",
							iconClass: "glyphicon glyphicon-star editClassColor",
							findBy:"month plan",
							value:value,
						});
                		$scope.notifictionCount++;
                	});
                	
                	angular.forEach(data.invitationData, function(value, key) {
                		$scope.notificationArray.push({
							title: "New meeting invitation received",
							iconClass: "fa fa-car editClassColorBlack",
							findBy:"invitation received",
							value:value,
						});
                		$scope.notifictionCount++;
                	});
                	
                	angular.forEach(data.declineMeeting, function(value, key) {
                		$scope.notificationArray.push({
							title: "Your invitation to "+value.assignedTo.firstName+" "+value.assignedTo.lastName+" has been declined",
							iconClass: "glyphicon glyphicon-star editClassColor",
							findBy:"declined meeting",
							value:value,
						});
                		$scope.notifictionCount++;
                	});
                	
                	
                	angular.forEach(data.comingSoonData, function(value, key) {
                		$scope.notificationArray.push({
							title: value.make+" "+value.model+" coming Soon Vehicle",
							iconClass: "fa fa-car editClassColorBlack",
							findBy:"coming soon",
							value:value,
						});
                		$scope.notifictionCount++;
                	});
                	
                	angular.forEach(data.acceptedMeeting, function(value, key) {
                		$scope.notificationArray.push({
							title: value.assignedTo.firstName+"  "+value.assignedTo.lastName+" accepted your invitation to "+value.name,
							iconClass: "glyphicon glyphicon-star editClassColor",
							findBy:"accept meeting",
							value:value,
						});
                		$scope.notifictionCount++;
                	});
                	
                	angular.forEach(data.deleteMeeting, function(value, key) {
                		if(value.declineUser == 'Host'){
                			$scope.notificationArray.push({
    							title: value.name+" has been cancelled",
    							iconClass: "glyphicon glyphicon-star editClassColor",
    							findBy:"delete meeting",
    							value:value,
    						});
                		
                		}else if(value.declineUser == 'this person'){
                			$scope.notificationArray.push({
    							title: value.firstName+" "+value.lastName+" can't go to the "+value.name,
    							iconClass: "glyphicon glyphicon-star editClassColor",
    							findBy:"delete meeting",
    							value:value,
    						});
                		}
                		
                		$scope.notifictionCount++;
                	});
                	
                	
                	angular.forEach(data.updateMeeting, function(value, key) {
                		$scope.notificationArray.push({
							title: value.name+" information has been changed",
							iconClass: "glyphicon glyphicon-star editClassColor",
							findBy:"update meeting",
							value:value,
						});
                		$scope.notifictionCount++;
                	});
                	
                	angular.forEach(data.reminderPopup, function(value, key) {
                		$scope.notificationArray.push({
							title: value.notes,
							iconClass: "fa fa-car editClassColorBlack",
							findBy:"reminder popup",
							value:value,
						});
                		$scope.notifictionCount++;
                	});
                	
                	$scope.decline(data.declineMeeting);
                	$scope.invitationMsg(data.invitationData);
                	$scope.likeMsg(data.commentLike);
                	$scope.planMsg(data.planScheduleMonthly);
                	$scope.acceptMsg(data.acceptedMeeting);
                	$scope.deleteMeeting(data.deleteMeeting);
                	$scope.updateMeeting(data.updateMeeting);
                	
            	});
            }
            
            
        	$scope.likeMsg = function(data) {

				
									angular.forEach(data, function(value, key) {
										var notifContent = '<div class="alert alert-dark media fade in bd-0" id="message-alert"><div class="media-left"></div>'
											+ '<div class="media-body width-100p col-md-12" style="padding: 0px;"><div class="col-md-3" style="padding: 0px;"><img style="width: 120px;" src="'+value.imageUrl+'"></div><div class="col-md-9"><div class="col-md-12" style="text-align: center;"><h2 style="color: goldenrod;margin-top: 0px;">Congratulations!</h2></div><span class="col-md-12" style="margin-left: 22px;text-align: center;"><h3 style="margin-top: 0px;"><span>'+value.firstName+'  '+ value.lastName+'</span><br><span> just Liked your work!</span><br><span>'+value.userComment+'</span></h3></span><p class="pull-left" style="margin-left:85%;"><a class="f-12">Close&nbsp;</a></p></div></div>'
											+ '</div>';
									var position = 'topRight';
									if ($('body').hasClass(
											'rtl'))
										position = 'topLeft';
									var n = noty({
										text : notifContent,
										type : 'success',
										layout : position,
										theme : 'made',
										animation : {
											open : 'animated bounceIn',
											close : 'animated bounceOut'
										},

										callback : {
											onShow : function() {
												$(
														'#noty_topRight_layout_container, .noty_container_type_success')
														.css(
																
																'width',
																410)
														.css('margin-left', -82)
														.css(
																
																'bottom',
																10);
											},
											onCloseClick : function() {
												$('html, body')
														.animate(
																{
																	scrollTop : 480
																},
																'slow');
											}
										}
									});
									});
					

			}
        	
	$scope.planMsg = function(data){
				
	    				var notifContent;
	    				angular.forEach(data, function(value, key) {
	    					var month=value.month;
	    					if(month !=null){
	    						month = month.toLowerCase();
	    						month=month.substring(0,1).toUpperCase()+month.substring(1);
	    					}
	    					
	    				notifContent = "<div class='alert alert-dark media fade in bd-0' id='message-alert'><div class='media-left'></div><div class='media-body width-100p'><p class='row' style='margin-left:0;'><span> "+month+"'s plan has been assigned</span></p></div></div>";
	    				
	    				var position = 'topRight';
		    	        if ($('body').hasClass('rtl'))
		    	        	position = 'topLeft';
		    	        var n = noty({
							text : notifContent,
							type : 'success',
							layout : position,
							theme : 'made',
							buttons: [
							          {
							              addClass: 'general-button customLeft', text: 'See', onClick: function($noty)
							              {
							            	  
							            	  $scope.planForsalePersonForMonth(value.month);
							                 $noty.close();
							              }
							          }
									 ],
							animation : {
								open : 'animated bounceIn',
								close : 'animated bounceOut'
							},

							callback : {
								onShow : function() {
									$(
											'#noty_topRight_layout_container, .noty_container_type_success')
											.css(
													
													'width',
													477)
											.css('margin-left', -135)
											.css(
													
													'bottom',
													10);
								},
								onCloseClick : function() {
									$('html, body')
											.animate(
													{
														scrollTop : 480
													},
													'slow');
								}
							}
						});
		    	        
		    	        var element = $('#cnt');
						//$compile(element)($scope);
	    				});
			}
            
	
	$scope.planForsalePersonForMonth = function(month){
		   $('#salepersonPlanModelForMonth').modal();
		   $http.get('/getPlanByMonthAndUser/'+userKey+'/'+month)
			.success(function(data) {
				if(data != null){
					$scope.monthFlagForSale=1;
				}
				$scope.saleMonthTotalPerForMonth=data;
			});
	   }
            

	$scope.invitationMsg = function(data) {

							angular.forEach(data, function(value, key) {
								var notifContent = '<div class="alert alert-dark media fade in bd-0" id="message-alert"><div class="media-left"></div>'
									+ '<div class="media-body width-100p col-md-12" style="padding: 0px;"><div class="col-md-3" style="padding: 0px;"><img style="width: 120px;" src="'+value.imageUrl+'"></div><div class="col-md-9"><div class="col-md-12" style="text-align: center;"><h3 style="margin-top: 0px;">New meeting invitation received</h3></div><span class="col-md-12" style="margin-left: 22px;text-align: center;border-bottom: solid;"><h3><span>'+value.name+'</span><br><span style="color: cornflowerblue;"><b>'+value.confirmDate+'&nbsp;&nbsp;&nbsp;&nbsp;'+value.confirmTime+' </b></span></h3></span><hr><p class="pull-left" style="margin-left:85%;"></p></div></div>'
									+ '</div>';
							var position = 'topRight';
							if ($('body').hasClass(
									'rtl'))
								position = 'topLeft';
							var n = noty({
								text : notifContent,
								type : 'success',
								layout : position,
								theme : 'made',
								buttons: [
								          {
										        addClass: 'general-button btnText', text: 'Decline', onClick: function($noty)
										              {
										            	  $scope.declineDate(value);
										                 $noty.close();
										              }
										          },
								          {
								              addClass: 'general-button btnText', text: 'Accept', onClick: function($noty)
								              {
								            	  $scope.acceptDate(value);
								                 $noty.close();
								              }
								          }
										 ],
								animation : {
									open : 'animated bounceIn',
									close : 'animated bounceOut'
								},

								callback : {
									onShow : function() {
										$(
												'#noty_topRight_layout_container, .noty_container_type_success')
												.css(
														
														'width',
														477)
												.css('margin-left', -135)
												.css(
														
														'bottom',
														10);
									},
									onCloseClick : function() {
										$('html, body')
												.animate(
														{
															scrollTop : 480
														},
														'slow');
									}
								}
							});
							});
			

	}
		
	
	$scope.declineDate = function(value){
		$('#decline-model').modal();
		$scope.valueId = value;
	}
	
	$scope.acceptDate = function(value){
		var reason = null;
		
		 $http.get('/getAcceptAndDecline/'+value.id+"/"+reason+"/"+"accept")
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Meeting invitation has been accepted",
				});
				
				$scope.schedulmultidatepicker();
				$http.get("/getscheduletest").success(function(data){
					   $scope.scheduleListData = data;
				   });
				
			});
		
	}
	
	$scope.decline = function(data){
		
		
				var notifContent;
				angular.forEach(data, function(value, key) {
				notifContent = "<div class='alert alert-dark media fade in bd-0' id='message-alert'><div class='media-left'></div><div class='media-body width-100p'><p class='row' style='margin-left:0;'><span> Your invitation to "+value.assignedTo.firstName+"&nbsp;&nbsp;"+value.assignedTo.lastName+" has been declined</span><br><span> Reason: "+value.declineReason+"</span></p><p class='row' style='margin-left:0;'></p><p class='pull-left' style='margin-left:65%;'><a class='f-12'>Close&nbsp;<i></i></a></p></div></div>";
				
				var position = 'topRight';
    	        if ($('body').hasClass('rtl')) position = 'topLeft';
    	        var n = noty({
    	            text: notifContent,
    	            type: 'success',
    	            layout: position,
    	            theme: 'made',
    	            animation: {
    	                open: 'animated bounceIn',
    	                close: 'animated bounceOut'
    	            },
    	            
    	            callback: {
    	                onShow: function () {
    	                    $('#noty_topRight_layout_container, .noty_container_type_success').css('width', 350).css('bottom', 10);
    	                },
    	                onCloseClick: function () {
    	                	$('html, body').animate({scrollTop:480}, 'slow');
    	                }
    	            }
    	        });
    	        
    	        var element = $('#cnt');
				//$compile(element)($scope);
				});
		
	}
	
	
	$scope.acceptMsg = function(data){

		
				var notifContent;
				angular.forEach(data, function(value, key) {
				notifContent = "<div class='alert alert-dark media fade in bd-0' id='message-alert'><div class='media-left'></div><div class='media-body width-100p'><p class='row' style='margin-left:0;'><span>"+value.assignedTo.firstName+"&nbsp;&nbsp;"+value.assignedTo.lastName+" accepted your invitation to "+value.name+"</span></p><p class='row' style='margin-left:0;'></p><p class='pull-left' style='margin-left:65%;'><a class='f-12'>Close&nbsp;<i></i></a></p></div></div>";
				
				var position = 'topRight';
    	        if ($('body').hasClass('rtl')) position = 'topLeft';
    	        var n = noty({
    	            text: notifContent,
    	            type: 'success',
    	            layout: position,
    	            theme: 'made',
    	            animation: {
    	                open: 'animated bounceIn',
    	                close: 'animated bounceOut'
    	            },
    	            
    	            callback: {
    	                onShow: function () {
    	                    $('#noty_topRight_layout_container, .noty_container_type_success').css('width', 350).css('margin-left', -30).css('bottom', 10);
    	                },
    	                onCloseClick: function () {
    	                	$('html, body').animate({scrollTop:480}, 'slow');
    	                }
    	            }
    	        });
    	        
    	        var element = $('#cnt');
				//$compile(element)($scope);
				});
	}
	
	
	$scope.deleteMeeting = function(data){
		

				var notifContent;
				angular.forEach(data, function(value, key) {
					if(value.declineUser == 'Host'){
						notifContent = "<div class='alert alert-dark media fade in bd-0' id='message-alert'><div class='media-left'></div><div class='media-body width-100p'><p class='row' style='margin-left:0;'><span>"+value.name+" has been cancelled</span><br></p><p class='row' style='margin-left:0;'></p><p class='pull-left' style='margin-left:65%;'><a class='f-12'>Close&nbsp;<i></i></a></p></div></div>";
	    				
	    				var position = 'topRight';
		    	        if ($('body').hasClass('rtl')) position = 'topLeft';
		    	        var n = noty({
		    	            text: notifContent,
		    	            type: 'success',
		    	            layout: position,
		    	            theme: 'made',
		    	            animation: {
		    	                open: 'animated bounceIn',
		    	                close: 'animated bounceOut'
		    	            },
		    	            
		    	            callback: {
		    	                onShow: function () {
		    	                    $('#noty_topRight_layout_container, .noty_container_type_success').css('width', 350).css('bottom', 10);
		    	                },
		    	                onCloseClick: function () {
		    	                	$('html, body').animate({scrollTop:480}, 'slow');
		    	                }
		    	            }
		    	        });
		    	        
		    	        var element = $('#cnt');
						//$compile(element)($scope);
					}else if(value.declineUser == 'this person'){
						notifContent = "<div class='alert alert-dark media fade in bd-0' id='message-alert'><div class='media-left'></div><div class='media-body width-100p'><p class='row' style='margin-left:0;'><span> "+value.firstName+"&nbsp;&nbsp;"+value.lastName+" can't go to the "+value.name+"</span><br><span>"+value.confirmDate+"&nbsp;&nbsp"+value.confirmTime+"</span><br><span>"+value.reason+"</span></p><p class='row' style='margin-left:0;'></p><p class='pull-left' style='margin-left:65%;'><a class='f-12'>Close&nbsp;<i></i></a></p></div></div>";
	    				
	    				var position = 'topRight';
		    	        if ($('body').hasClass('rtl')) position = 'topLeft';
		    	        var n = noty({
		    	            text: notifContent,
		    	            type: 'success',
		    	            layout: position,
		    	            theme: 'made',
		    	            animation: {
		    	                open: 'animated bounceIn',
		    	                close: 'animated bounceOut'
		    	            },
		    	            
		    	            callback: {
		    	                onShow: function () {
		    	                    $('#noty_topRight_layout_container, .noty_container_type_success').css('width', 350).css('bottom', 10);
		    	                },
		    	                onCloseClick: function () {
		    	                	$('html, body').animate({scrollTop:480}, 'slow');
		    	                }
		    	            }
		    	        });
		    	        
		    	        var element = $('#cnt');
						//$compile(element)($scope);
					}
				
				});
		
		
	}
	
	$scope.updateMeeting = function(data){

				var notifContent;
				angular.forEach(data, function(value, key) {
				//var t = $filter('date')(value.confirmTime,"hh:mm a");
				notifContent = "<div class='alert alert-dark media fade in bd-0' id='message-alert'><div class='media-left'></div><div class='media-body width-100p'><p class='row' style='margin-left:0;'><span>"+value.name+" information has been changed</span><br><span>"+value.confirmDate+"   "+value.confirmTime+" - "+value.confirmEndTime+"</span><br><span>"+value.reason+"</span></p><p class='row' style='margin-left:0;'></p><p class='pull-left' style='margin-left:65%;'><a class='f-12'>Close&nbsp;<i></i></a></p></div></div>";
				
				var position = 'topRight';
    	        if ($('body').hasClass('rtl')) position = 'topLeft';
    	        var n = noty({
    	            text: notifContent,
    	            type: 'success',
    	            layout: position,
    	            theme: 'made',
    	            animation: {
    	                open: 'animated bounceIn',
    	                close: 'animated bounceOut'
    	            },
    	            
    	            callback: {
    	                onShow: function () {
    	                    $('#noty_topRight_layout_container, .noty_container_type_success').css('width', 350).css('margin-left', -30).css('bottom', 10);
    	                },
    	                onCloseClick: function () {
    	                	$('html, body').animate({scrollTop:480}, 'slow');
    	                }
    	            }
    	        });
    	        
    	        var element = $('#cnt');
				//$compile(element)($scope);
				});
	}
	
	
	
	
	
	
        }]);
