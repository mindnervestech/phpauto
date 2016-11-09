angular.module('formlyIonic', ['formly'], ["formlyConfigProvider", function configFormlyIonic(formlyConfigProvider) {
    'use strict';


    angular.forEach(['checkbox', 'input', 'radio', 'range', 'textarea', 'toggle', 'select', 'floating-input', 'stacked-input', 'inline-input','signature','ui-grid','image','financialcalculator','daterange','autocompleteText','contactssearch','inventorysearch','fileuploaders','timerange','headerlabel','numberInput','multipleselect','singleSelect','leadTypeSelector'], function (fieldName) {
        if(fieldName === 'signature'){
            formlyConfigProvider.setType({
                name: fieldName,
                templateUrl: getFieldTemplateUrl(fieldName),
                controller:'SignatureCTRl'
            });
        }else if(fieldName === 'ui-grid'){
            formlyConfigProvider.setType({
                name: fieldName,
                templateUrl: getFieldTemplateUrl(fieldName)
                //template: '<div  style ="height: 1px !important;" ui-grid="{ data: model[options.key], columnDefs: to.columnDefs, onRegisterApi: to.onRegisterApi}"></div>'
                //wrapper: ['bootstrapLabel', 'bootstrapHasError']
            });
        }else if(fieldName === 'financialcalculator'){
            formlyConfigProvider.setType({
                name: fieldName,
                templateUrl: getFieldTemplateUrl(fieldName),
                controller:'customizationCtrl'
            });
        }else if(fieldName === 'contactssearch'){
            formlyConfigProvider.setType({
                name: fieldName,
                templateUrl: getFieldTemplateUrl(fieldName),
               controller:'customizationCtrl'
            });
        } 
        else if(fieldName === 'numberInput'){
            formlyConfigProvider.setType({
                name: fieldName,
                templateUrl: getFieldTemplateUrl(fieldName),
                controller:'customizationCtrl'
            });
        }
        
        else if(fieldName === 'singleSelect'){
            formlyConfigProvider.setType({
                name: fieldName,
                templateUrl: getFieldTemplateUrl(fieldName),
                controller:'customizationCtrl'
            });
        }
        
        else if(fieldName === 'autocompleteText'){
            formlyConfigProvider.setType({
                name: fieldName,
                templateUrl: getFieldTemplateUrl(fieldName),
                controller:'customizationCtrl'
            });
        }else if(fieldName === 'inventorysearch'){
            formlyConfigProvider.setType({
                name: fieldName,
                templateUrl: getFieldTemplateUrl(fieldName),
                controller:'customizationCtrl'
            });
        }else if(fieldName === 'fileuploaders'){
            formlyConfigProvider.setType({
                name: fieldName,
                templateUrl: getFieldTemplateUrl(fieldName),
                controller:'customizationCtrl'
            });
        }else if(fieldName === 'timerange'){
            formlyConfigProvider.setType({
                name: fieldName,
                templateUrl: getFieldTemplateUrl(fieldName),
                controller:'customizationCtrl'
            });
        }else if(fieldName === 'multipleselect'){
            formlyConfigProvider.setType({
                name: fieldName,
                templateUrl: getFieldTemplateUrl(fieldName),
                controller:'customizationCtrl'
            });
        }else if(fieldName === 'leadTypeSelector'){
            formlyConfigProvider.setType({
                name: fieldName,
                templateUrl: getFieldTemplateUrl(fieldName),
                controller:'customizationCtrl'
            });
        }else{
            formlyConfigProvider.setType({
                name: fieldName,
                templateUrl: getFieldTemplateUrl(fieldName)
            });
        }
    });

    formlyConfigProvider.templateManipulators.preWrapper.push(function ariaDescribedBy(template, options, scope) {
        if (options.templateOptions && angular.isDefined(options.templateOptions.description) &&
            options.type !== 'radio' && options.type !== 'checkbox') {
            var el = angular.element('<a></a>');
            el.append(template);
            var modelEls = angular.element(el[0].querySelectorAll('[ng-model]'));
            if (modelEls) {
                el.append(
                    '<p id="' + scope.id + '_description"' +
                    'class="help-block"' +
                    'ng-if="options.templateOptions.description">' +
                    '{{options.templateOptions.description}}' +
                    '</p>'
                );
                modelEls.attr('aria-describedby', scope.id + '_description');
                return el.html();
            } else {
                return template;
            }
        } else {
            return template;
        }
    });

    function getFieldTemplateUrl(name) {
        return 'fields/' + name + '.html';
    }


}]);

angular.module("formlyIonic")
.controller('SignatureCTRl',function($scope){
    console.log('SignatureCTRl');
    var app = this;
    app.signature = {};
    app.close = function(){
      if (app.signature.isEmpty()) {
          app.showRequiredMsg = true;
      } else {
          app.hasSignature = true;
      }
    }
})

.run(["$templateCache", function($templateCache) {
//$templateCache.put("fields/ion-checkbox.html","<label style=\"margin-left: 18px;font-size:17px;\">{{options.name}}</label><div class=\"list\"><ion-checkbox ng-repeat=\"item in options.templateOptions.label\" ng-model=\"model[item.id]\">{{item.value}}</ion-checkbox></div>");
//$templateCache.put("fields/ion-checkbox.html","<label style=\"margin-left: 18px;font-size:17px;\">{{options.name}}</label><div class=\"list\"><ion-checkbox ng-repeat=\"item in options.templateOptions.label\" ng-model=\"model[item.id]\" formly-dynamic-name=\"id\" formly-custom-validation=\"options.validators\" ng-required=\"options.templateOptions.required\" ng-disabled=\"options.templateOptions.disabled\">{{item.value}}</ion-checkbox></div>");
$templateCache.put("fields/checkbox.html","<label class=\"item item-input item-stacked-label\"><span class=\"input-label\">{{options.name}}</span></label><div class=\"list\"><ion-checkbox ng-repeat=\"item in options.templateOptions.label\" ng-model=\"model[item.id]\">{{item.value}}</ion-checkbox></div>");

$templateCache.put("fields/floating-input.html","<label class=\"item item-input item-floating-label form-group col-sm-12\" style=\"padding-left: 0px;\"><span class=\"input-label form-group col-sm-2\">{{options.templateOptions.label}}</span> <div class=\"col-sm-4 form-group\"><input class=\"col-sm-10 form-control form-white\" ng-model=\"model[options.key]\" placeholder=\"{{options.templateOptions.placeholder}}\" type=\"{{options.templateOptions.type}}\"></div></label>");
$templateCache.put("fields/inline-input.html","<label class=\"item item-input form-group col-sm-12\" style=\"padding-left: 0px;\"><span class=\"input-label form-group col-sm-2\">{{options.templateOptions.label}}</span> <div class=\"col-sm-4 form-group\"><input class=\"col-sm-10 form-control form-white\" ng-model=\"model[options.key]\" placeholder=\"{{options.templateOptions.placeholder}}\" type=\"{{options.templateOptions.type}}\"></div></label>");
$templateCache.put("fields/input.html","<label class=\"item item-input form-group col-sm-12\" style=\"padding-left: 0px;\"><i class=\"icon {{options.templateOptions.icon}}\" ng-if=\"options.templateOptions.icon\" ng-class=\"{\'placeholder-icon\': options.templateOptions.iconPlaceholder}\"></i><div class=\"col-sm-4 form-group\"><input class=\"col-sm-10 form-control form-white\" ng-model=\"model[options.key]\" placeholder=\"{{options.templateOptions.placeholder}}\" type=\"{{options.templateOptions.type}}\"></div></label>");

//$templateCache.put("fields/ion-radio.html","<label style=\"margin-left: 18px;font-size:17px;\">{{options.name}}</label><ion-radio ng-repeat=\"item in options.templateOptions.options\" icon=\"{{item.icon? item.icon: \'ion-checkmark\'}}\" ng-value=\"item.value\" ng-model=\"model[options.key]\">{{ item.text }}</ion-radio>");
$templateCache.put("fields/radio.html","<label class=\"item item-input item-stacked-label\"><span class=\"input-label\">{{options.name}}</span></label><div class=\"list\"><ion-radio ng-repeat=\"item in options.templateOptions.options\" icon=\"{{item.icon? item.icon: \'ion-checkmark\'}}\" ng-value=\"item.value\" ng-model=\"model[options.key]\">{{ item.text }}</ion-radio></div>");

$templateCache.put("fields/range.html","<div class=\"item range\" ng-class=\"\'range-\' + options.templateOptions.rangeClass\"><span class=\"col-sm-3\" style=\"text-align: right;\">{{options.templateOptions.label}}</span> <i class=\"icon\" ng-if=\"options.templateOptions.minIcon\" ng-class=\"options.templateOptions.minIcon\"></i> <input type=\"range\" min=\"{{options.templateOptions.min}}\" max=\"{{options.templateOptions.max}}\" step=\"{{options.templateOptions.step}}\" value=\"{{options.templateOptions.value}}\" ng-model=\"model[options.key]\"> <i class=\"icon\" ng-if=\"options.templateOptions.maxIcon\" ng-class=\"options.templateOptions.maxIcon\"></i></div>");
$templateCache.put("fields/select.html","<label class=\"item item-input item-select form-group col-sm-12\" style=\"padding-left: 0px;\"><div class=\"input-label form-group col-sm-2\" style=\"text-align: right;\">{{to.label}}</div><div class=\"col-sm-4 form-group\"><select class=\"col-sm-10 form-control form-white\" ng-model=\"model[options.key]\" ng-options=\"option[to.valueProp || \'value\'] as option[to.labelProp || \'name\'] group by 			  option[to.groupProp || \'group\'] for option in to.options\"></select></div></label>");

$templateCache.put("fields/leadTypeSelector.html","<label class=\"item item-input item-select form-group col-sm-12\" style=\"padding-left: 0px;\"><div class=\"input-label form-group col-sm-2\" style=\"text-align: right;\">{{to.label}}</div><div class=\"col-sm-4 form-group\"><select class=\"col-sm-10 form-control form-white\" ng-model=\"model[options.key]\"><option ng-repeat=\"lType in leadList\">{{lType.leadName}}</option></select></div></label>");
//$templateCache.put("fields/ion-stacked-input.html","<label class=\"item item-input item-stacked-label\"><span class=\"input-label\">{{options.templateOptions.label}}</span> <input ng-model=\"model[options.key]\" placeholder=\"{{options.templateOptions.placeholder}}\" type=\"{{options.templateOptions.type}}\"></label>");
$templateCache.put("fields/stacked-input.html","<label class=\"item item-input item-stacked-label form-group col-sm-12\" style=\"padding-left: 0px;\"><span class=\"input-label form-group col-sm-2\" style=\"text-align: right;\">{{options.templateOptions.label}}</span><div class=\"col-sm-4 form-group\"><input class=\"col-sm-10 form-control form-white\" ng-model=\"model[options.key]\" formly-dynamic-name=\"id\" formly-custom-validation=\"options.validators\" placeholder=\"{{options.templateOptions.placeholder}}\" ng-required=\"options.templateOptions.required\" ng-disabled=\"options.templateOptions.disabled\"></div></label>");

$templateCache.put("fields/image.html","<label class=\"item item-input item-stacked-label\"><img  ng-src=\"{{options.templateOptions.imageUrl}}\" id=\"{{options.templateOptions.label}}\" ng-model=\"model[options.key]\" style=\"cursor:pointer;\" ng-click=\"takePic($event)\"></label>");

// $templateCache.put("fields/ion-textarea.html","<span ng-click = \"addTextArea()\" ng-show = \"options.templateOptions.isRepeatable || iFrameModel.textObj.isRepeat\" style = \"cursor : pointer; float: right;\" class=\"ion-android-add\"></span><div  ng-repeat = \"text in iFrameModel.textObj\" > <span ng-show = \"iFrameModel.textObj[$index].isChild\" ng-click = \"remove(iFrameModel)\" class=\"ion-minus\" style = \"cursor : pointer; float:right;\"></span> <label class=\"item item-input item-stacked-label\"><span class=\"input-label\">{{options.templateOptions.label}}</span><textarea rows=\"{{options.templateOptions.rows? options.templateOptions.rows: \'5\'}}\" placeholder=\"{{options.templateOptions.placeholder}}\" ng-model = \"iFrameModel.textObj[$index].textarea\"></textarea></label><div>" );
// $templateCache.put("fields/ion-textarea.html","<span ng-click = \"addTextArea()\" ng-show = \"options.templateOptions.isRepeatable || iFrameModel.textObj.isRepeat\" style = \"cursor : pointer; float: right;\" class=\"ion-android-add\"></span><div  ng-repeat = \"text in iFrameModel.textObj\" > <span ng-show = \"iFrameModel.textObj[$index].isChild\" ng-click = \"remove(iFrameModel.textObj[$index])\" class=\"ion-minus\" style = \"cursor : pointer; float:right;\"></span> <label class=\"item item-input item-stacked-label\"><span class=\"input-label\">{{options.templateOptions.label}}</span><textarea rows=\"{{options.templateOptions.rows? options.templateOptions.rows: \'5\'}}\" placeholder=\"{{options.templateOptions.placeholder}}\" ng-model = \"model[options.key]\"></textarea></label><div>" );
$templateCache.put("fields/textarea.html","<label class=\"item item-input item-stacked-label form-group col-sm-12\" style=\"padding-left: 0px;\"><span class=\"input-label form-group col-sm-2\" style=\"text-align: right;\">{{options.templateOptions.label}}</span><div class=\"col-sm-4 form-group\"><textarea class=\"col-sm-10\" rows=\"{{options.templateOptions.rows? options.templateOptions.rows: \'5\'}}\" placeholder=\"{{options.templateOptions.placeholder}}\" ng-model=\"model[options.key]\"></textarea></div></label>");

// $templateCache.put("fields/ion-textarea.html","<span style = \"cursor : pointer;\" class=\"ion-android-add\"></span>" );

// $templateCache.put("fields/ion-textarea.html","src/template/textareaTemplate.html");

//$templateCache.put("fields/ion-signature.html","<div style=\"height: 250px;\"><label style=\"margin-left: 18px;font-size:17px;\">{{options.templateOptions.label}}</label><div signature-pad signature=\"app.signature\" close=\"app.close()\"></div><div></div></div><div><div></div></div>");
$templateCache.put("fields/signature.html","<div style=\"height: 250px;\"><label class=\"item item-input item-stacked-label\" style=\"border-bottom: none;\"><span class=\"input-label\">{{options.templateOptions.label}}</span></label><div signature-pad signature=\"app.signature\" close=\"app.close()\"></div></div>");

$templateCache.put("fields/ui-grid.html","<div class=\"item item-input item-stacked-label\" >{{options.templateOptions.label}}  <a style=\"cursor:pointer;\" ng-click=\"addRow()\">add</a> </div><div style =\"height: 100% !important;\" ui-grid=\"{ data: model[options.key], columnDefs: to.columnDefs, onRegisterApi: to.onRegisterApi,enableColumnMenus: to.enableColumnMenus,enableSorting: to.enableSorting}\" ui-grid-edit ui-grid-row-edit ui-grid-cellNav class=\"grid\" ng-dblclick=\"getFocus()\"></div>");

//$templateCache.put("fields/ion-ui-grid.html","<label class=\"item item-input item-stacked-label\"><span class=\"input-label\">{{options.templateOptions.label}}  <a style=\"cursor:pointer;\" ng-click=\"addRow()\">add</a> </span> </label><div style =\"height: 100% !important;\" ui-grid=\"{ data: model[options.key], columnDefs: to.columnDefs, onRegisterApi: to.onRegisterApi,enableColumnMenus: to.enableColumnMenus,enableSorting: to.enableSorting}\" ui-grid-edit ui-grid-row-edit ui-grid-cellNav class=\"grid\" ng-dblclick=\"getFocus()\"></div>");
$templateCache.put("fields/financialcalculator.html","<div class=\"form-group col-sm-11\" style=\"margin-left: 14px;\"><h4 class=\"modal-title\" style=\"color:#c7081b;\"><b>Financial Calculator</b></h4></div><div class=\"col-sm-12\"><div class=\"financing_calculator\"><div class=\"form-group col-sm-12\"><label class=\"col-sm-6 control-label\" style=\"text-align: left;\">Cost of Vehicle($)</label><div class=\"col-sm-6\"><input type=\"text\" ng-model=\"financeData.price\" class=\"number cost\"></div></div><div class=\"form-group col-sm-12\"><label class=\"col-sm-6 control-label\" style=\"text-align: left;\">Down Payment($)</label><div class=\"col-sm-6\"><input type=\"text\" ng-model=\"financeData.downPayment\" class=\"number down_payment\"></div></div><div class=\"form-group col-sm-12\"><label class=\"col-sm-6 control-label\" style=\"text-align: left;\">Annual Interest Rate($)</label><div class=\"col-sm-6\"><input type=\"text\" ng-model=\"financeData.annualInterestRate\" class=\"number interest\"></div></div><div class=\"form-group col-sm-12\"><label class=\"col-sm-6 control-label\" style=\"text-align: left;\">Term of Loan in Years:</label><div class=\"col-sm-6\"><input type=\"text\" ng-model=\"financeData.numberOfYears\" class=\"number loan_years\"></div></div><div class=\"form-group col-sm-12\"><label class=\"col-sm-6 control-label\" style=\"text-align: left;\">Frequency of Payments:($)</label><div class=\"col-sm-6\" ><select ng-model=\"financeData.frequencyOfPayments\" class=\"frequency css-dropdowns\"><option value=\"26\">Monthly</option><option value=\"52\">Bi-Weekly</option><option value=\"12\">Weekly</option></select></div></div><div class=\"form-group col-sm-12\"><label class=\"col-sm-3 control-label\" style=\"text-align: left;\"></label><div class=\"col-sm-6\"><button type=\"button\" class=\"btn btn-default btn-embossed  pull-right\" ng-click=\"calculateFinancialData(financeData)\" style=\"background-color: #c7081b;color: white;\">Calculate Payment</button></div></div><div class=\"form-group col-sm-12\"><strong class=\"col-sm-6\" style=\"text-align: left;\">NUMBER OF PAYMENTS:</strong><strong class=\"payments col-sm-6\">{{payments}}</strong></div><div class=\"form-group col-sm-12\"><strong class=\"col-sm-6\" style=\"text-align: left;\">PAYMENT AMOUNT:</strong><strong class=\"payment_amount\" >{{payment}}</strong></div></div></div>");
$templateCache.put("fields/daterange.html","<label class=\"item item-input form-group col-sm-12\" style=\"padding-left: 0px;\"><i class=\"icon {{options.templateOptions.icon}}\" ng-if=\"options.templateOptions.icon\" ng-class=\"{\'placeholder-icon\': options.templateOptions.iconPlaceholder}\"></i><label class=\"col-sm-2 form-group\">Start Date</label><div class=\"col-sm-4 form-group\"><input ng-model=\"model['startDate']\" name=\"startDate\" type=\"{{options.templateOptions.type}}\"></div><i class=\"icon {{options.templateOptions.icon}}\" ng-if=\"options.templateOptions.icon\" ng-class=\"{\'placeholder-icon\': options.templateOptions.iconPlaceholder}\"></i><label class=\"col-sm-2 form-group\">End Date</label><div><input class=\"col-sm-4\" name=\"endDate\" ng-model=\"model['endDate']\" min=\"{{startDate}}\" type=\"{{options.templateOptions.type}}\"></div></label>");
$templateCache.put("fields/autocompleteText.html","<div class=\"form-group col-sm-12\" id=\"{{formName+index | nospace}}\">\n    <label class=\"col-sm-2 control-label\" for=\"{{formName+index}}\" ng-class=\"{'fb-required':required}\" style=\"text-align: right;\"><i ng-if =\"formObject.logic.component\" id=\"hasLogic\" class=\"fa fa-random label-logic\" style=\"text-align: right;\"></i>{{options.templateOptions.label}}</label>\n    <div class=\"col-sm-6\">\n   <input type=\"text\" ng-model=\"model[options.key]\" id=\"autocomplete\" list=\"comicstitle\"  class=\"form-control form-white\" ng-change=\"getbusinessname()\"/><datalist id=\"comicstitle\"><option  data-ng-repeat=\"ttl in locationNameList\" value=\"{{ttl.description}}\" ></datalist>\n    </div>");


$templateCache.put("fields/contactssearch.html","<div class=\"col-md-12\" style=\"padding-left: 0px;\"><div ng-show=\"isInValid\" style=\"color: red;\">Please provide all mandatory fields.</div><h3 style=\"margin-top:10px; margin-bottom: 10px;\"><strong>{{editInput.mainTitle}}</strong></h3><div class=\"col-md-6\"><div class=\"col-md-12\" style=\"margin: 10px 0 10px 0;\"><angucomplete-alt id=\"exCustoms\" placeholder=\"Search Name\" pause=\"10\" selected-object=\"selectedObjs\" local-data=\"searchList\" search-fields=\"fullName\" title-field=\"fullName\" minlength=\"3\" input-class=\"form-control form-control-small\"></angucomplete-alt></div><div class=\"col-md-12\" style=\"margin: 10px 0 10px 0;\"><input type=\"text\" ng-model=\"model['custNumber']\" placeholder=\"PHONE NUMBER\" required/> <span style=\"color: red;\">*</span></div></div><div class=\"col-md-6\"><div class=\"col-md-12\" style=\"margin: 10px 0 10px 0;\"><input type=\"text\" ng-model=\"model['custEmail']\" placeholder=\"EMAIL\" required/> <span style=\"color: red;\">*</span></div><div class=\"col-md-12\" style=\"margin: 10px 0 10px 0;\"><input type=\"text\" ng-model=\"model['custZipCode']\" placeholder=\"ZIP CODE\" required/> <span style=\"color: red;\">*</span></div></div></div>");
$templateCache.put("fields/inventorysearch.html","<div class=\"row\"><div class=\"col-md-12\" ng-repeat=\"stockRp in stockWiseData1\"><div class=\"col-md-4\"><div class=\"col-md-12\"><label>{{editInput.searchSubTitle}}</label></div><div ng-show=\"isStockError\" style=\"color: red;\">Not Found.</div><div class=\"col-md-12\" style=\"margin: 10px 0 10px 0;\"><div class=\"col-md-12\" style=\"margin: 10px 0 10px 0;\">	<input type=\"text\" ng-model=\"stockRp.stockNumber\" ng-blur=\"getStockDetails1(stockRp)\"/></div><angucomplete-alt id=\"vinSearch1\" placeholder=\"Title\" pause=\"10\" selected-object=\"selectedVin\" local-data=\"vinSearchList\" search-fields=\"title\" title-field=\"title\" minlength=\"3\" input-class=\"form-control form-control-small\"></angucomplete-alt></div><div class=\"col-md-12\" style=\"margin: 10px 0 10px 0;\"><input type=\"text\" ng-model=\"stockRp.model\" disabled=\"disabled\" placeholder=\"Model\"/></div><div class=\"col-md-12\" style=\"margin: 10px 0 10px 0;\"><input type=\"text\" ng-model=\"stockRp.year\" disabled=\"disabled\" placeholder=\"Year\"/></div></div><div class=\"col-md-7\" style=\"border-radius: 11px / 13px;border: 1px solid #e5e5e5;box-shadow: 0 0 5px;font-family: 'Open Sans', sans-serif;padding-left: 0px;\" ng-if=\"stockRp.vin != null\"><div><div class=\"col-md-5\" style=\"padding-left: 0px;\"><img class=\"preview\" style=\"margin-top: 7px;width: 174px;height: 121px;\" src=\"/getImage/{{stockRp.imgId}}/thumbnail\"></div><div class=\"col-md-7\" style=\"padding: 0px;\"><label class=\"col-md-12\" style=\"font-size: 20px;padding: 0px;\">{{stockRp.year}} {{stockRp.make}} {{stockRp.model}}</label><label class=\"col-md-6\">Body Style:</label><label class=\"col-md-6\">{{stockRp.bodyStyle}}</label><label class=\"col-md-6\">price:</label><label class=\"col-md-6\" ng-if=\"stockRp.price != null\">{{stockRp.price}}</label><label class=\"col-md-6\" ng-if=\"stockRp.price == null\">_</label><label class=\"col-md-6\">Mileage</label><label class=\"col-md-6\" ng-if=\"stockRp.mileage != null\">{{stockRp.mileage}}</label><label class=\"col-md-6\" ng-if=\"stockRp.mileage == null\">_</label><label class=\"col-md-6\">year:</label><label class=\"col-md-6\" ng-if=\"stockRp.year != null\">{{stockRp.year}}</label><label class=\"col-md-6\" ng-if=\"stockRp.year == null\">_</label><label class=\"col-md-6\">Transmission:</label><label class=\"col-md-6\" ng-if=\"stockRp.transmission != null\">{{stockRp.transmission}}</label><label class=\"col-md-6\" ng-if=\"stockRp.transmission == null\">_</label><label class=\"col-md-6\">Engine:</label><label class=\"col-md-6\" ng-if=\"stockRp.engine != null\">{{stockRp.engine}}</label><label class=\"col-md-6\" ng-if=\"stockRp.engine == null\">_</label></div></div></div><div class=\"col-md-1\" style=\"font-size: 29px;margin-top: 54px;\" ng-show=\"stockWiseData.length  > 1\"><a ng-click=\"removeLead($index)\"><span class=\"glyphicon glyphicon-trash\"></span></a></div></div><div class=\"col-md-12\" style=\"text-align: center;font-size: 17px;\" ng-if=\"stockWiseData[0].vin != null\"><a ng-click=\"pushRecord()\">ADD ITEM</a></div></div>");
$templateCache.put("fields/fileuploaders.html","<div class=\"form-group col-sm-12\"><label class=\"col-sm-2 control-label\" style=\"padding-left: 0px;text-align: right;\">{{options.templateOptions.label}}</label><div class=\"col-sm-6\"><input type=\"file\" ng-model=\"model['files']\" name=\"logoFile\" ng-file-select=\"onLogoFileSelect($files)\" accept=\"application/msword,application/pdf\"/></div>");
$templateCache.put("fields/timerange.html","<div class=\"form-group col-sm-12\"><label class=\"col-sm-2 control-label\" style=\"padding-left: 0px;text-align: right;\">{{options.templateOptions.label}}</label><div class=\"form-group col-sm-4 prepend-icon\"><input type=\"text\" name=\"timepicker\" class=\"timepicker form-control form-white hasDatepicker\" ng-model=\"model['timeSet']\"  placeholder=\"Choose a time...\" ng-click=\"showtimepick()\" data-format=\"am-pm\" id=\"bestTimes\"><i class=\"icon-clock\"></i></div>");
$templateCache.put("fields/headerlabel.html","<label class=\"item item-input item-stacked-label form-group col-sm-12\" style=\"padding-left: 0px;\"><h2><span class=\"input-label form-group col-sm-12\"><b>{{options.templateOptions.label}}</b></span></h2></label>");
$templateCache.put("fields/numberInput.html","<div   style=\"padding: 0px;\" class=\"col-sm-12\"><label for=\"{{formName+index}}\" class=\"col-md-4 control-label\">{{label}}</label> <div  style=\"padding: 0px;\" class=\"col-sm-7\">\n    <input type=\"number\" class=\"form-control m-b\" />\n </div></div>");
$templateCache.put("fields/multipleselect.html","<label class=\"item item-input item-select form-group col-sm-12\" style=\"padding-left: 0px;\"><div class=\"input-label form-group col-sm-2\" style=\"text-align: right;\">{{to.label}}</div><div class=\"col-sm-4 form-group\"><select class=\"col-sm-10 form-control form-white\" ng-model=\"model[options.key]\" ng-options=\"option[to.valueProp || \'value\'] as option[to.labelProp || \'name\'] group by 			  option[to.groupProp || \'group\'] for option in to.options\" multiple></select></div></label>");

$templateCache.put("fields/singleSelect.html","<div class=\"form-group col-sm-12\" > <label class=\"item item-input item-select form-group col-sm-12\" style=\"padding-left: 0px;\"><div class=\"input-label form-group col-sm-2\" style=\"text-align: right;\">{{to.label}}</div><div   class=\"col-sm-4 form-group\"> <select  style=\"width: 125px\"  ng-model=\"model[options.key]\" data-ng-attr-size=\"{{to.options.length}}\"><option  ng-repeat=\"option in to.options\">{{option[to.labelProp || \'name\']}}</option></select></div></label></div>");

/*<div  class=\"dropdown open\"> <ul class=\"dropdown-menu\"><li ng-repeat=\"option in to.options\" ng-model=\"model[options.key]\" id=\"{{formName+index}}\"><a >{{option[to.labelProp || \'name\']}}</a></li>  </ul></div>
*/
/*ng-model=\"model[options.key]\" ng-options=\"option[to.valueProp || \'value\'] as option[to.labelProp || \'name\'] group by 			  option[to.groupProp || \'group\'] for option in to.options\"></select></div></label>");*/


$templateCache.put("fields/toggle.html","<ion-toggle ng-model=\"model[options.key]\" toggle-class=\"toggle-{{options.templateOptions.toggleClass}}\">{{options.templateOptions.label}}</ion-toggle>");}]);

