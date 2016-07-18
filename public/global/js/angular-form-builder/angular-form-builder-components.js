(function() {
  angular.module('builder.components', ['builder', 'validator.rules']).config([
    '$builderProvider', function($builderProvider) {
      /*$builderProvider.registerComponent('textInput', {
        group: 'Default',
        label: 'Text Input',
        description: 'description',
        placeholder: 'placeholder',
        required: false,
        validationOptions: [
          {
            label: 'none',*/
            //rule: '/.*/'
          /*}, {
            label: 'number',
            rule: '[number]'
          }, {
            label: 'email',
            rule: '[email]'
          }, {
            label: 'url',
            rule: '[url]'
          }
        ],
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-md-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-md-8\">\n        <input type=\"text\" ng-model=\"inputText\" validator-required=\"{{required}}\" validator-group=\"{{formName}}\" id=\"{{formName+index}}\" class=\"form-control\" placeholder=\"{{placeholder}}\"/>\n        <p class='help-block'>{{description}}</p>\n    </div>\n</div>",
        popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Description</label>\n        <input type='text' ng-model=\"description\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Placeholder</label>\n        <input type='text' ng-model=\"placeholder\" class='form-control'/>\n    </div>\n    <div class=\"checkbox\">\n        <label>\n            <input type='checkbox' ng-model=\"required\" />\n            Required</label>\n    </div>\n    <div class=\"form-group\" ng-if=\"validationOptions.length > 0\">\n        <label class='control-label'>Validation</label>\n        <select ng-model=\"$parent.validation\" class='form-control' ng-options=\"option.rule as option.label for option in validationOptions\"></select>\n    </div>\n\n    <hr/>\n    <div class='form-group'>\n        <input type='submit' ng-click=\"popover.save($event)\" class='btn btn-primary' value='Save'/>\n        <input type='button' ng-click=\"popover.cancel($event)\" class='btn btn-default' value='Cancel'/>\n        <input type='button' ng-click=\"popover.remove($event)\" class='btn btn-danger' value='Delete'/>\n    </div>\n</form>"
      });*/
      
      $builderProvider.registerComponent('textArea', {
        group: 'Default',
        label: 'Text Area', 
        key:'text_area',
        description: '* instructions',
        required: false,
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-md-3 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-md-6\">\n        <textarea type=\"text\" ng-model=\"inputText\" validator-required=\"{{required}}\" validator-group=\"{{formName}}\" id=\"{{formName+index}}\" class=\"form-control\" rows='6' placeholder=\"{{placeholder}}\"/>\n        <p class='help-block'>{{description}}</p>\n    </div>\n <div class='col-sm-12'> <hr> </div>  </div>",
        //popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Description</label>\n        <input type='text' ng-model=\"description\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Placeholder</label>\n        <input type='text' ng-model=\"placeholder\" class='form-control'/>\n    </div>\n    <div class=\"checkbox\">\n        <label>\n            <input type='checkbox' ng-model=\"required\" />\n            Required</label>\n    </div>\n\n    <hr/>\n    <div class='form-group'>\n        <input type='submit' ng-click=\"popover.save($event)\" class='btn btn-primary' value='Save'/>\n        <input type='button' ng-click=\"popover.cancel($event)\" class='btn btn-default' value='Cancel'/>\n        <input type='button' ng-click=\"popover.remove($event)\" class='btn btn-danger' value='Delete'/>\n    </div>\n</form>"
        popoverTemplateUrl: '/dealer/template/textarea.html'
      });
      /*$builderProvider.registerComponent('checkbox', {
        group: 'Default',
        label: 'Checkbox',
        description: 'description',
        placeholder: 'placeholder',
        required: false,
        options: ['value one', 'value two'],
        arrayToText: true,
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-md-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-md-8\">\n        <input type='hidden' ng-model=\"inputText\" validator-required=\"{{required}}\" validator-group=\"{{formName}}\"/>\n        <div class='checkbox' ng-repeat=\"item in options track by $index\">\n            <label><input type='checkbox' ng-model=\"$parent.inputArray[$index]\" value='item'/>\n                {{item}}\n            </label>\n        </div>\n        <p class='help-block'>{{description}}</p>\n    </div>\n</div>",
        popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Description</label>\n        <input type='text' ng-model=\"description\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Options</label>\n        <textarea class=\"form-control\" rows=\"3\" ng-model=\"optionsText\"/>\n    </div>\n    <div class=\"checkbox\">\n        <label>\n            <input type='checkbox' ng-model=\"required\" />\n            Required\n        </label>\n    </div>\n\n    <hr/>\n    <div class='form-group'>\n        <input type='submit' ng-click=\"popover.save($event)\" class='btn btn-primary' value='Save'/>\n        <input type='button' ng-click=\"popover.cancel($event)\" class='btn btn-default' value='Cancel'/>\n        <input type='button' ng-click=\"popover.remove($event)\" class='btn btn-danger' value='Delete'/>\n    </div>\n</form>"
      });*/
      /*$builderProvider.registerComponent('radio', {
        group: 'Default',
        label: 'Radio',
        description: 'description',
        placeholder: 'placeholder',
        required: false,
        options: ['value one', 'value two'],
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-md-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-md-8\">\n        <div class='radio' ng-repeat=\"item in options track by $index\">\n            <label><input name='{{formName+index}}' ng-model=\"$parent.inputText\" validator-group=\"{{formName}}\" value='{{item}}' type='radio'/>\n                {{item}}\n            </label>\n        </div>\n        <p class='help-block'>{{description}}</p>\n    </div>\n</div>",
        // popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>Label1</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Description</label>\n        <input type='text' ng-model=\"description\" class='form-control'/>\n    </div>\n <div class=\"form-group\">\n        <label class='control-label'>Name</label>\n        <input type='text' ng-model=\"key\" class='form-control'/>\n    </div>\n   <div class=\"form-group\">\n        <label class='control-label'>Options</label>\n        <textarea class=\"form-control\" rows=\"3\" ng-model=\"optionsText\"/>\n    </div>\n\n    <hr/>\n    <div class='form-group'>\n        <input type='submit' ng-click=\"popover.save($event)\" class='btn btn-primary' value='Save'/>\n        <input type='button' ng-click=\"popover.cancel($event)\" class='btn btn-default' value='Cancel'/>\n        <input type='button' ng-click=\"popover.remove($event)\" class='btn btn-danger' value='Delete'/>\n    </div>\n</form>"
        popoverTemplateUrl: '/dealer/template/radio.html'
      });*/
    $builderProvider.registerComponent('textInput', {
        group: 'Default',
        label: 'Text Input',
        key:'text_input',
        description: '',
        readOnly: false,
        minLength: 0,
        maxLength: 999,
        minRange: 0,
        maxRange: 99999,
        required: false,
        validationOptions: [
          {
            label: 'None',
            rule: '/.*/'
          }, {
            label: 'Text',
            rule: '[text]'
          }, {
            label: 'Number',
            rule: '[numberRange]'
          }
        ],
        template: "<div class=\"form-group col-sm-12\" id=\"{{formName+index | nospace}}\">\n    <label class=\"col-sm-3 control-label\" for=\"{{formName+index}}\" ng-class=\"{'fb-required':required}\"><i ng-if =\"formObject.logic.component\" id=\"hasLogic\" class=\"fa fa-random label-logic\"></i> {{label}}</label>\n    <div class=\"col-sm-6\">\n        <input type=\"text\" ng-show=\"validation != '[numberRange]'\" ng-readonly=\"readOnly\" ng-model=\"inputText\" validator-required=\"{{required}}\" validator-group=\"{{formName}}\" class=\"form-control m-b\" placeholder=\"{{placeholder}}\"/>\n        <input type=\"tel\" ng-show=\"validation === '[numberRange]'\" ng-readonly=\"readOnly\" ng-model=\"inputText\" validator-required=\"{{required}}\" validator-group=\"{{formName}}\" class=\"form-control m-b\" placeholder=\"{{placeholder}}\"/>\n    </div>\n  <div class=\"col-sm-10 col-sm-offset-2\">\n    <small ng-show=\"description\" class=\"help-block text-muted custom-small\">{{description}}</small>\n  </div>\n</div>\n<div id=\"dashedline\" class=\"hr-line-dashed\"> <div class='col-sm-12'> <hr> </div> </div>",
        //popoverTemplate: "<form>\n\n    <div role=\"tabpanel\">\n\n        <!-- Nav tabs -->\n        <ul class=\"nav nav-justified nav-tabs\" role=\"tablist\" style=\"margin-left:-10px\">\n            <li role=\"presentation\" class=\"active\"><a href=\"{{'#properties' + date + index}}\" aria-controls=\"{{'properties' + date + index}}\" role=\"tab\" data-toggle=\"tab\">Properties</a></li>\n            <li role=\"presentation\"><a href=\"{{'#validations' + date + index}}\" aria-controls=\"{{'validations' + date + index}}\" role=\"tab\" data-toggle=\"tab\">Validations</a></li>\n            <li role=\"presentation\"><a href=\"{{'#logic' + date + index}}\" aria-controls=\"{{'logic' + date + index}}\" role=\"tab\" data-toggle=\"tab\">Logic</a></li>\n            <li role=\"presentation\"><a href=\"{{'#points' + date + index}}\" aria-controls=\"{{'points' + date + index}}\" role=\"tab\" data-toggle=\"tab\">Points</a></li>\n        </ul>\n\n        <!-- Tab panes -->\n        <div class=\"tab-content\">\n            <div role=\"tabpanel\" class=\"tab-pane active\" id=\"{{'properties' + date + index}}\">\n                <div class=\"form-group m-t-sm\">\n                    <label class='control-label'>Label</label>\n                    <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n                </div>\n                <div class=\"form-group\">\n                    <label class='control-label'>Description</label>\n                    <input type='text' ng-model=\"description\" class='form-control'/>\n                </div>\n                <div class=\"form-group\">\n                    <label class='control-label'>Placeholder</label>\n                    <input type='text' ng-model=\"placeholder\" class='form-control'/>\n                </div>\n                <div class=\"checkbox\">\n                    <label>\n                        <input type='checkbox'  ng-model=\"readOnly\" />\n                        Read Only</label>\n                </div>\n            </div>\n            <div role=\"tabpanel\" class=\"tab-pane\" id=\"{{'validations' + date + index}}\">\n                <div class=\"checkbox m-t\">\n                    <label>\n                        <input type='checkbox'  ng-model=\"required\" />\n                        Required</label>\n                </div>\n                <div class=\"form-group\" ng-if=\"validationOptions.length > 0\">\n                    <label class='control-label'>Validation</label>\n                    <select ng-model=\"$parent.validation\" class='form-control' ng-options=\"option.rule as option.label for option in validationOptions\"></select>\n                </div>\n                <div class=\"row\" ng-show=\"validation==='[text]'\">\n                    <div class=\"form-group col-sm-6\">\n                        <input type=\"text\" class=\"form-control\" ng-model=\"minLength\" placeholder=\"Min Length\">\n                    </div>\n                    <div class=\"form-group col-sm-6\">\n                        <input type=\"text\" class=\"form-control\" ng-model=\"maxLength\" placeholder=\"Max Length\">\n                    </div>\n                </div>\n                <div class=\"row\" ng-show=\"validation==='[numberRange]'\">\n                    <div class=\"form-group col-sm-6\">\n                        <input type=\"text\" class=\"form-control\" ng-model=\"minRange\" placeholder=\"Min Range\">\n                    </div>\n                    <div class=\"form-group col-sm-6\">\n                        <input type=\"text\" class=\"form-control\" ng-model=\"maxRange\" placeholder=\"Max Range\">\n                    </div>\n                    <div class=\"form-group col-sm-6\">\n                        <input type=\"text\" class=\"form-control\" ng-model=\"minLength\" placeholder=\"Min Length\">\n                    </div>\n                    <div class=\"form-group col-sm-6\">\n                        <input type=\"text\" class=\"form-control\" ng-model=\"maxLength\" placeholder=\"Max Length\">\n                    </div>\n                </div>\n            </div>\n            <div role=\"tabpanel\" class=\"tab-pane\" id=\"{{'logic' + date + index}}\">\n                <div class=\"form-group m-t\">\n                    <select class=\"form-control custom-m-b\" ng-model=\"formObject.logic.action\" ng-options=\"action for action in actions\"></select><p> this element if</p>\n                    <component-selector></component-selector>\n\n                    <select class=\"form-control custom-m-b\" ng-model=\"formObject.logic.comparator\" ng-options=\"comparator for comparator in comparatorChoices\">\n                    </select>\n                    <input type=\"text\" ng-model=\"formObject.logic.value\" class=\"form-control\" placeholder=\"Value\">\n                    <button type=\"button\" ng-click=\"resetLogic()\" class=\"btn btn-default pull-right\">Reset</button>\n                    <br>\n                </div>\n            </div>\n            <div role=\"tabpanel\" class=\"tab-pane p-t-n\" id=\"{{'points' + date + index}}\">\n\n              <div class=\"form-group\" ng-if=\"formObject.pointRules.length > 0\">\n                <label class=\"control-label m-t-c--5\">Active Rules</label>\n                <div class=\"m-t-xs\" ng-repeat=\"rule in formObject.pointRules\">\n                  Add <span class=\"label label-primary\">{{rule.points}}</span> points if this field <span class=\"label label-primary\">{{rule.predicate | predicate}}</span>&nbsp;<span class=\"label label-primary\" ng-if=\"rule.predicate !== 'null' && rule.predicate !== 'not_null'\">{{rule.value}}</span> <a ng-click=\"removeRule(rule)\" class=\"pull-right btn btn-sm btn-link hover-dark-grey-link btn-xs\"><i class=\"fa fa-times-circle\"></i></a>\n                </div>\n              </div>\n              <div class=\"form-group custom-m-b\">\n                  <div class=\"input-group\">\n                    <span class=\"input-group-addon add-points\">Add</span>\n                    <input type=\"text\" class=\"form-control\" ng-model=\"newRule.points\">\n                    <span class=\"input-group-addon add-points\">points</span>\n                  </div>\n              </div>\n\n              <div class=\"form-group custom-m-b\">\n                <div class=\"input-group\">\n                  <span class=\"input-group-addon add-points\">if this field</span>\n                  <select ng-model=\"newRule.predicate\" ng-options=\"predicate.value as predicate.label for predicate in predicates\" class=\"form-control\"></select>\n                </div>\n              </div>\n\n              <div class=\"input-group\">\n                <input ng-readonly=\"newRule.predicate === 'null' || newRule.predicate === 'not_null'\" ng-model=\"newRule.value\" type=\"text\" class=\"form-control\" placeholder=\"value\">\n                <span class=\"input-group-btn\">\n                  <button ng-click=\"addRule()\" class=\"btn btn-primary\" type=\"button\">+</button>\n                </span>\n              </div>\n              <p class=\"text-danger\">{{rulesErrorMessage}}</p>\n\n            </div>\n        </div>\n    </div>\n\n    <hr/>\n    <div class='form-group'>\n        <input type='button' ng-click=\"popover.remove($event)\" class='btn btn-danger fa h-c-34 pull-right m-b m-l-xs' value='&#xf1f8'/>\n        <input type='submit' ng-click=\"popover.save($event)\" class='btn btn-primary h-c-34 pull-right m-b fa' value='&#xf0c7'/>\n        <input type='button' ng-click=\"popover.cancel($event)\" class='btn btn-white h-c-34 pull-left m-b' value='Cancel'/>\n    </div>\n</form>"
        popoverTemplateUrl: '/dealer/template/textInput.html'
      });
    $builderProvider.registerComponent('date', {
        group: 'Default',
        label: 'Date Selector',
        key:'date_selector',
        description: '',
        required: false,
        disableWeekends: false,
        readOnly: false,
        displayTemplate:"<div class=\"form-group col-sm-12\" id=\"{{formName+index | nospace}}\">\ndate</div>",
        template: "<div class=\"form-group col-sm-12\" id=\"{{formName+index | nospace}}\">\n      <label class=\"col-sm-3 control-label\" for=\"{{formName+index}}\" ng-class=\"{'fb-required':required}\"><i ng-if =\"formObject.logic.component\" id=\"hasLogic\" class=\"fa fa-random label-logic\"></i> {{label}}</label>\n      <div class=\"col-sm-6\">\n        <ui-date weekends=\"{{disableWeekends}}\"></ui-date>\n      </div>\n    <div class=\"col-sm-10 col-sm-offset-2\">\n      <small ng-show=\"description\" class=\"help-block text-muted custom-small\">{{description}}</small>\n    </div>\n</div>\n<div id=\"dashedline\" class=\"hr-line-dashed\"> <div class='col-sm-12'> <hr> </div></div>",
        //popoverTemplate: "<form>\n\n    <div role=\"tabpanel\">\n\n        <!-- Nav tabs -->\n        <ul class=\"nav nav-justified nav-tabs\" role=\"tablist\" style=\"margin-left:-10px\">\n            <li role=\"presentation\" class=\"active\"><a href=\"{{'#properties' + date + index}}\" aria-controls=\"{{'properties' + date + index}}\" role=\"tab\" data-toggle=\"tab\">Properties</a></li>\n            <li role=\"presentation\"><a href=\"{{'#validations' + date + index}}\" aria-controls=\"{{'validations' + date + index}}\" role=\"tab\" data-toggle=\"tab\">Validations</a></li>\n            <li role=\"presentation\"><a href=\"{{'#logic' + date + index}}\" aria-controls=\"{{'logic' + date + index}}\" role=\"tab\" data-toggle=\"tab\">Logic</a></li>\n            <li role=\"presentation\"><a href=\"{{'#points' + date + index}}\" aria-controls=\"{{'points' + date + index}}\" role=\"tab\" data-toggle=\"tab\">Points</a></li>\n        </ul>\n\n        <!-- Tab panes -->\n        <div class=\"tab-content\">\n            <div role=\"tabpanel\" class=\"tab-pane active\" id=\"{{'properties' + date + index}}\">\n                <div class=\"form-group m-t-sm\">\n                    <label class='control-label'>Label</label>\n                    <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n                </div>\n                <div class=\"form-group\">\n                    <label class='control-label'>Description</label>\n                    <input type='text' ng-model=\"description\" class='form-control'/>\n                </div>\n                <div class=\"checkbox\">\n                    <label>\n                        <input type='checkbox'  ng-model=\"readOnly\" />\n                        Read Only</label>\n                </div>\n            </div>\n            <div role=\"tabpanel\" class=\"tab-pane\" id=\"{{'validations' + date + index}}\">\n                <div class=\"checkbox m-t\">\n                    <label>\n                        <input type='checkbox'  ng-model=\"required\" />\n                        Required</label>\n                </div>\n                <div class=\"checkbox\">\n                    <label>\n                        <input type='checkbox'  ng-model=\"disableWeekends\" />\n                        Disable Weekends</label>\n                </div>\n                <div class=\"form-group\" ng-if=\"validationOptions.length > 0\">\n                    <label class='control-label'>Validation</label>\n                    <select ng-model=\"$parent.validation\" class='form-control' ng-options=\"option.rule as option.label for option in validationOptions\"></select>\n                </div>\n                <div class=\"form-group\">\n                    <div class=\"row\">\n                        <div class=\"col-sm-3\">\n                            Date is in next\n                        </div>\n                        <div class=\"col-sm-7\">\n                            <select class=\"form-control\" ng-model=\"nextXDays\" ng-options=\"value for value in [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30]\"></select>\n                        </div>\n                        <div class=\"col-sm-2\">\n                            days\n                        </div>\n                    </div>\n                </div>\n            </div>\n            <div role=\"tabpanel\" class=\"tab-pane\" id=\"{{'logic' + date + index}}\">\n                <div class=\"form-group m-t\">\n                    <select class=\"form-control custom-m-b\" ng-model=\"formObject.logic.action\" ng-options=\"action for action in actions\"></select><p> this element if</p>\n                    <component-selector></component-selector>\n\n                    <select class=\"form-control custom-m-b\" ng-model=\"formObject.logic.comparator\" ng-options=\"comparator for comparator in comparatorChoices\">\n                    </select>\n                    <input type=\"text\" ng-model=\"formObject.logic.value\" class=\"form-control\" placeholder=\"Value\">\n                    <button type=\"button\" ng-click=\"resetLogic()\" class=\"btn btn-default pull-right\">Reset</button>\n                    <br>\n                </div>\n            </div>\n            <div role=\"tabpanel\" class=\"tab-pane p-t-n\" id=\"{{'points' + date + index}}\">\n\n              <div class=\"form-group\" ng-if=\"formObject.pointRules.length > 0\">\n                <label class=\"control-label m-t-c--5\">Active Rules</label>\n                <div class=\"m-t-xs\" ng-repeat=\"rule in formObject.pointRules\">\n                  Add <span class=\"label label-primary\">{{rule.points}}</span> points if this field <span class=\"label label-primary\">{{rule.predicate | predicate}}</span>&nbsp;<span class=\"label label-primary\" ng-if=\"rule.predicate !== 'null' && rule.predicate !== 'not_null'\">{{rule.value}}</span> <a ng-click=\"removeRule(rule)\" class=\"pull-right btn btn-sm btn-link hover-dark-grey-link btn-xs\"><i class=\"fa fa-times-circle\"></i></a>\n                </div>\n              </div>\n              <div class=\"form-group custom-m-b\">\n                  <div class=\"input-group\">\n                    <span class=\"input-group-addon add-points\">Add</span>\n                    <input type=\"text\" class=\"form-control\" ng-model=\"newRule.points\">\n                    <span class=\"input-group-addon add-points\">points</span>\n                  </div>\n              </div>\n\n              <div class=\"form-group custom-m-b\">\n                <div class=\"input-group\">\n                  <span class=\"input-group-addon add-points\">if this field</span>\n                  <select ng-model=\"newRule.predicate\" ng-options=\"predicate.value as predicate.label for predicate in predicates\" class=\"form-control\"></select>\n                </div>\n              </div>\n\n              <div class=\"input-group\">\n                <input ng-readonly=\"newRule.predicate === 'null' || newRule.predicate === 'not_null'\" ng-model=\"newRule.value\" type=\"text\" class=\"form-control\" placeholder=\"value\">\n                <span class=\"input-group-btn\">\n                  <button ng-click=\"addRule()\" class=\"btn btn-primary\" type=\"button\">+</button>\n                </span>\n              </div>\n              <p class=\"text-danger\">{{rulesErrorMessage}}</p>\n\n            </div>\n\n        </div>\n    </div>\n\n    <hr/>\n    <div class='form-group'>\n        <input type='button' ng-click=\"popover.remove($event)\" class='btn btn-danger fa h-c-34 pull-right m-b m-l-xs' value='&#xf1f8'/>\n        <input type='submit' ng-click=\"popover.save($event)\" class='btn btn-primary h-c-34 pull-right m-b fa' value='&#xf0c7'/>\n        <input type='button' ng-click=\"popover.cancel($event)\" class='btn btn-white h-c-34 pull-left m-b' value='Cancel'/>\n    </div>\n</form>"
        popoverTemplateUrl: '/dealer/template/datepicker.html'
      });
     
      $builderProvider.registerComponent('select', {
        group: 'Default',
        label: 'Dropdown',
        key:'dropdown',
        required: false,
        options: ['value one', 'value two'],
        template: "<div class=\"form-group col-sm-12\">\n    <label for=\"{{formName+index}}\" class=\"col-md-3 control-label\">{{label}}</label>\n    <div class=\"col-md-6\">\n        <select ng-options=\"value for value in options\" id=\"{{formName+index}}\" class=\"form-control\"\n            ng-model=\"inputText\" ng-init=\"inputText = options[0]\"/>\n        <p class='help-block'>{{description}}</p>\n    </div>\n <div class='col-sm-12'> <hr> </div></div>",
        //popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Description</label>\n        <input type='text' ng-model=\"description\" class='form-control'/>\n    </div>\n   <div class=\"form-group\">\n        <label class='control-label'>Name</label>\n        <input type='text' ng-model=\"key\" class='form-control'/>\n    </div>\n   <div class=\"form-group\">\n        <label class='control-label'>Options</label>\n        <textarea class=\"form-control\" rows=\"3\" ng-model=\"optionsText\"/>\n    </div>\n\n    <hr/>\n    <div class='form-group'>\n        <input type='submit' ng-click=\"popover.save($event)\" class='btn btn-primary' value='Save'/>\n        <input type='button' ng-click=\"popover.cancel($event)\" class='btn btn-default' value='Cancel'/>\n        <input type='button' ng-click=\"popover.remove($event)\" class='btn btn-danger' value='Delete'/>\n    </div>\n</form>"
        popoverTemplateUrl: '/dealer/template/select.html'
      });
      
      $builderProvider.registerComponent('financialcalculator', {
          group: 'Default',
          label: 'Financial Calculator',
          key:'financial_calculator',
          description: '',
          disableWeekends: false,
          readOnly: false,
          template: "<div class=\"form-group col-sm-11\" style=\"margin-left: 14px;padding: 0px;\"><h4 class=\"modal-title\" style=\"color:#c7081b;\"><b>Financial Calculator</b></h4></div><div style='padding: 0px;'  class=\"col-sm-12\"><div class=\"financing_calculator\"><div class=\"form-group col-sm-12\"><label class=\"col-sm-6 control-label\" style=\"text-align: left;\">Cost of Vehicle($)</label><div class=\"col-sm-6\"><input type=\"text\" ng-model=\"financeData.price\" class=\"number cost\"></div></div><div class=\"form-group col-sm-12\"><label class=\"col-sm-6 control-label\" style=\"text-align: left;\">Down Payment($)</label><div class=\"col-sm-6\"><input type=\"text\" class=\"number down_payment\"></div></div><div class=\"form-group col-sm-12\"><label class=\"col-sm-6 control-label\" style=\"text-align: left;\">Annual Interest Rate($)</label><div class=\"col-sm-6\"><input type=\"text\" ng-model=\"financeData.annualInterestRate\" class=\"number interest\"></div></div><div class=\"form-group col-sm-12\"><label class=\"col-sm-6 control-label\" style=\"text-align: left;\">Term of Loan in Years:</label><div class=\"col-sm-6\"><input type=\"text\" class=\"number loan_years\"></div></div><div class=\"form-group col-sm-12\"><label class=\"col-sm-6 control-label\" style=\"text-align: left;\">Frequency of Payments:($)</label><div class=\"col-sm-6\" ><select class=\"frequency css-dropdowns\"><option value=\"26\">Monthly</option><option value=\"52\">Bi-Weekly</option><option value=\"12\">Weekly</option></select></div></div><div class=\"form-group col-sm-12\"><label class=\"col-sm-3 control-label\" style=\"text-align: left;\"></label><div class=\"col-sm-6\"><button type=\"button\" class=\"btn btn-default btn-embossed  pull-right\" ng-click=\"calculateFinancialData(financeData)\" style=\"background-color: #c7081b;color: white;\">Calculate Payment</button></div></div><div class=\"form-group col-sm-12\"><strong class=\"col-sm-6\" style=\"text-align: left;\">NUMBER OF PAYMENTS:</strong><strong class=\"payments col-sm-6\">{{payments}}</strong></div><div class=\"form-group col-sm-12\"><strong class=\"col-sm-6\" style=\"text-align: left;\">PAYMENT AMOUNT:</strong><strong class=\"payment_amount\" >{{payment}}</strong></div></div>  <div class='col-sm-12'> <hr> </div> </div>",
          popoverTemplateUrl: '/dealer/template/select.html'
        });
      //id="autocomplete"
      $builderProvider.registerComponent('daterange', {
          group: 'Default',
          label: 'Date Range',
          key:'date_range',
          description: '',
          required: false,
          disableWeekends: false,
          readOnly: false,
          displayTemplate:"<div class=\"form-group col-sm-12\" id=\"{{formName+index | nospace}}\">\ndate</div>",
          template: "<div style=\"margin-left: 2px;padding: 0px;\" class=\"form-group col-sm-12\"><div class=\"form-group col-sm-6\"><label class=\"form-group col-sm-5\">Start Date</label><div class=\"form-group col-sm-9\"><ui-date weekends=\"{{disableWeekends}}\"></ui-date></div></div><div class=\"form-group col-sm-6\"><label class=\"form-group col-sm-5\">End Date</label><div class=\"form-group col-sm-9\"><ui-date weekends=\"{{disableWeekends}}\"></ui-date></div></div> <div class='col-sm-12'> <hr> </div> </div>",
          popoverTemplateUrl: '/dealer/template/datepicker.html'
        });
      
      $builderProvider.registerComponent('autocompleteText', {
          group: 'Default',
          label: 'Address bar',
          key:'address_bar',
          description: '',
          readOnly: false,
          required: false,
         // template: "<div class=\"form-group col-sm-12\" id=\"{{formName+index | nospace}}\">\n    <label class=\"col-sm-3 control-label\" for=\"{{formName+index}}\" ng-class=\"{'fb-required':required}\"><i ng-if =\"formObject.logic.component\" id=\"hasLogic\" class=\"fa fa-random label-logic\"></i> {{label}}</label>\n    <div class=\"col-sm-6\">\n  <angucomplete-alt id=\"ex1\" placeholder=\"Search Name\" pause=\"10\" selected-object=\"selectedNames\" local-data=\"prodSearchList\" search-fields=\"title\" title-field=\"title\" minlength=\"3\" input-class=\"form-control form-control-small\"></angucomplete-alt>\n</div>",
          template: "<div class=\"form-group col-sm-12\" style=\"padding: 0px;\" id=\"{{formName+index | nospace}}\">\n    <label class=\"col-sm-3 control-label\" for=\"{{formName+index}}\" ng-class=\"{'fb-required':required}\"><i ng-if =\"formObject.logic.component\" id=\"hasLogic\" class=\"fa fa-random label-logic\"></i> {{label}}</label>\n    <div class=\"col-sm-6\">\n        <input type=\"text\" id=\"autocomplete\"  ng-model=\"autoText\" class=\"form-control m-b\" placeholder=\"{{placeholder}}\"/>\n </div> <div class='col-sm-12'> <hr> </div> </div>",
          popoverTemplateUrl: '/dealer/template/textInput.html'
        });
      
      $builderProvider.registerComponent('contactssearch', {
          group: 'Default',
          label: 'Contacts Search',
          key:'contacts_search',
          description: '',
          disableWeekends: false,
          readOnly: false,
          template: "<div class=\"col-md-12\"><div ng-show=\"isInValid\" style=\"color: red;\">Please provide all mandatory fields.</div><h3 style=\"margin-top:10px; margin-bottom: 10px;\"><strong>{{editInput.mainTitle}}</strong></h3><div class=\"col-md-6\"><div class=\"col-md-12\" style=\"margin: 10px 0 10px 0;\"><angucomplete-alt id=\"ex1\" placeholder=\"Search Name\" pause=\"10\" selected-object=\"selectedObj\" local-data=\"searchList\" search-fields=\"fullName\" title-field=\"fullName\" minlength=\"3\" focus-out=\"focusOut()\" input-class=\"form-control form-control-small\"></angucomplete-alt></div><div class=\"col-md-12\" style=\"margin: 10px 0 10px 0;\"><input type=\"text\" ng-model=\"lead.custNumber\" placeholder=\"PHONE NUMBER\" required/> <span style=\"color: red;\">*</span></div></div><div class=\"col-md-6\"><div class=\"col-md-12\" style=\"margin: 10px 0 10px 0;\"><input type=\"text\" ng-model=\"lead.custEmail\" placeholder=\"EMAIL\" required/> <span style=\"color: red;\">*</span></div><div class=\"col-md-12\" style=\"margin: 10px 0 10px 0;\"><input type=\"text\" ng-model=\"lead.custZipCode\" placeholder=\"ZIP CODE\" required/> <span style=\"color: red;\">*</span></div></div> <div class='col-sm-12'> <hr> </div> </div>",
          popoverTemplateUrl: '/dealer/template/textInput.html'
        });
      
      
      $builderProvider.registerComponent('inventorysearch', {
          group: 'Default',
          label: 'Inventory Search',
          key:'inventory_search',
          description: '',
          disableWeekends: false,
          readOnly: false,
          template: "<div class=\"col-md-12\"><h3>&nbsp;&nbsp;&nbsp;&nbsp;<a ng-click=\"editLeadInfo('Search Title')\" ng-if=\"showSaveButton == 'Edit'\"><i class=\"glyphicon glyphicon-edit\"></i></a></h3><div class=\"col-md-4\"><div class=\"col-md-12\"><label>{{setjson.searchSubTitle}}</label>&nbsp;&nbsp;&nbsp;&nbsp;<a ng-click=\"editLeadInfo('Search Sub Title')\" ng-if=\"showSaveButton == 'Edit'\"><i class=\"glyphicon glyphicon-edit\"></i></a></div><div ng-show=\"isStockError\" style=\"color: red;\">Furniture Not Found.</div><div style=\"margin: 10px 0 10px 0;\"><input type=\"text\" class=\"col-md-12\" ng-model=\"stockRp.designer\" style=\"margin-bottom: 7px;\" disabled=\"disabled\" placeholder=\"Title\"/></div><div style=\"margin: 10px 0 10px 0;\"><input type=\"text\" class=\"col-md-12\" style=\"margin-bottom: 7px;\" ng-model=\"stockRp.designer\" disabled=\"disabled\" placeholder=\"Title\"/></div><div style=\"margin: 10px 0 10px 0;\"><input type=\"text\" class=\"col-md-12\" ng-model=\"stockRp.year\" style=\"margin-bottom: 7px;\" disabled=\"disabled\" placeholder=\"Year\"/></div></div><div class=\"col-md-7\" style=\"border-radius: 11px / 13px;border: 1px solid #e5e5e5;box-shadow: 0 0 5px;font-family: 'Open Sans', sans-serif;padding-left: 0px;\"><div><div class=\"col-md-5\" style=\"padding-left: 0px;\"><img class=\"preview col-md-5\" style=\"margin-top: 7px;width: 136px;height: 121px;\" src=\"/assets/images/car.jpg\"></div><div class=\"col-md-7\" style=\"padding: 0px;\"><label class=\"col-md-12\" style=\"font-size: 20px;padding: 0px;\">Furniture Name</label><label class=\"col-md-6\">designer:</label><label class=\"col-md-6\">-</label><label class=\"col-md-6\">price:</label><label class=\"col-md-6\">_</label><label class=\"col-md-6\">Primary Title:</label><label class=\"col-md-6\">_</label><label class=\"col-md-6\">year:</label><label class=\"col-md-6\">_</label></div></div></div><div class=\"col-md-1\" style=\"font-size: 29px;margin-top: 54px;\" ng-show=\"stockWiseData.length  > 1\"><a ng-click=\"removeLead($index)\"><span class=\"glyphicon glyphicon-trash\"></span></a></div></div><div class=\"col-md-12\" style=\"text-align: center;font-size: 17px;\"><a>ADD ITEM</a> <div class='col-sm-12'> <hr> </div></div>",
          popoverTemplateUrl: '/dealer/template/textInput.html'
        });
      
      $builderProvider.registerComponent('fileuploaders', {
          group: 'Default',
          label: 'File uploader',
          key:'file_uploader',
          description: '',
          disableWeekends: false,
          readOnly: false,
          template: "<div class=\"col-md-12\"><label for=\"{{formName+index}}\" class=\"col-md-4 control-label\">{{label}}</label><div class=\"col-md-8\"><input type=\"file\" name=\"logoFile\" ng-file-select=\"onLogoFileSelect($files)\" accept=\"application/msword,application/pdf\"/></div> <div class='col-sm-12'> <hr> </div> </div>",
          popoverTemplateUrl: '/dealer/template/textInput.html'
        });
      
      $builderProvider.registerComponent('headerlabel', {
          group: 'Default',
          label: 'Header label', 
          key:'header_label',
          description: '',
          required: false,
          template: "<div><h2><label for=\"{{formName+index}}\" class=\"col-md-12 control-label\"><b>{{label}}</b></label><h2> <div class='col-sm-12'> <hr> </div>  </div>",
          //popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Description</label>\n        <input type='text' ng-model=\"description\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Placeholder</label>\n        <input type='text' ng-model=\"placeholder\" class='form-control'/>\n    </div>\n    <div class=\"checkbox\">\n        <label>\n            <input type='checkbox' ng-model=\"required\" />\n            Required</label>\n    </div>\n\n    <hr/>\n    <div class='form-group'>\n        <input type='submit' ng-click=\"popover.save($event)\" class='btn btn-primary' value='Save'/>\n        <input type='button' ng-click=\"popover.cancel($event)\" class='btn btn-default' value='Cancel'/>\n        <input type='button' ng-click=\"popover.remove($event)\" class='btn btn-danger' value='Delete'/>\n    </div>\n</form>"
          popoverTemplateUrl: '/dealer/template/headerPop.html'
        });
      
      $builderProvider.registerComponent('timerange', {
    	  group: 'Default',
          label: 'Time Range',
          key:'time_range',
          description: '',
          required: false,
          disableWeekends: false,
          readOnly: false,
          displayTemplate:"<div class=\"form-group col-sm-12\" id=\"{{formName+index | nospace}}\">\ndate</div>",
          template: "<div class=\"form-group col-sm-12\"><label for=\"{{formName+index}}\" class=\"col-md-4 control-label\">{{label}}</label><div class=\"form-group col-sm-8\"><input type=\"text\" name=\"timepicker\" ng-model=\"testDriveData.confirmTime\" class=\"timepicker form-control hasDatepicker\" placeholder=\"Choose a time...\" data-format=\"am-pm\" id=\"bestTime\"> </div> <div class='col-sm-12'> <hr> </div> </div>",
          popoverTemplateUrl: '/dealer/template/datepicker.html'
        });
      
      
      $builderProvider.registerComponent('numberInput', {
          group: 'Default',
          label: 'Price', 
          key:'number_input',
          description: '',
          required: false,
          template: "<div   style=\"padding: 0px;\" class=\"col-sm-12\"><label for=\"{{formName+index}}\" class=\"col-md-4 control-label\">{{label}}</label> <div  style=\"padding: 0px;\" class=\"col-sm-7\">\n    <input type=\"number\" class=\"form-control m-b\" />\n </div> <div class='col-sm-12'> <hr> </div>  </div>",
          //popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Description</label>\n        <input type='text' ng-model=\"description\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Placeholder</label>\n        <input type='text' ng-model=\"placeholder\" class='form-control'/>\n    </div>\n    <div class=\"checkbox\">\n        <label>\n            <input type='checkbox' ng-model=\"required\" />\n            Required</label>\n    </div>\n\n    <hr/>\n    <div class='form-group'>\n        <input type='submit' ng-click=\"popover.save($event)\" class='btn btn-primary' value='Save'/>\n        <input type='button' ng-click=\"popover.cancel($event)\" class='btn btn-default' value='Cancel'/>\n        <input type='button' ng-click=\"popover.remove($event)\" class='btn btn-danger' value='Delete'/>\n    </div>\n</form>"
          popoverTemplateUrl: '/dealer/template/textInput.html'
        });
      
      $builderProvider.registerComponent('multipleselect', {
          group: 'Default',
          label: 'Multiple select',
          key:'dropdown',
          required: false,
          options: ['value one', 'value two', 'value three'],
          template: "<div class=\"form-group col-sm-12\">\n    <label for=\"{{formName+index}}\" class=\"col-md-3 control-label\">{{label}}</label>\n    <div class=\"col-md-6\">\n        <select ng-options=\"value for value in options\" id=\"{{formName+index}}\" class=\"form-control\"\n            ng-model=\"inputText\" ng-init=\"inputText = options[0]\" multiple/>\n        <p class='help-block'>{{description}}</p>\n    </div>\n <div class='col-sm-12'> <hr> </div></div>",
          //popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Description</label>\n        <input type='text' ng-model=\"description\" class='form-control'/>\n    </div>\n   <div class=\"form-group\">\n        <label class='control-label'>Name</label>\n        <input type='text' ng-model=\"key\" class='form-control'/>\n    </div>\n   <div class=\"form-group\">\n        <label class='control-label'>Options</label>\n        <textarea class=\"form-control\" rows=\"3\" ng-model=\"optionsText\"/>\n    </div>\n\n    <hr/>\n    <div class='form-group'>\n        <input type='submit' ng-click=\"popover.save($event)\" class='btn btn-primary' value='Save'/>\n        <input type='button' ng-click=\"popover.cancel($event)\" class='btn btn-default' value='Cancel'/>\n        <input type='button' ng-click=\"popover.remove($event)\" class='btn btn-danger' value='Delete'/>\n    </div>\n</form>"
          popoverTemplateUrl: '/dealer/template/select.html'
        });
      
     $builderProvider.registerComponent('singleSelect', {
          group: 'Default',
          label: 'single Selector',
          key:'single_select',
          required: false,
          options: ['value one', 'value two'],
          template: "<div class=\"form-group col-sm-12\">\n    <label for=\"{{formName+index}}\" class=\"col-md-3 control-label\">{{label}}</label>\n    <div class=\"col-md-6\">\n        <select  style=\"width: 125px\"  id=\"{{formName}}\" data-ng-attr-size=\"{{options.length}}\" ><option  ng-repeat=\"value in options\">{{value}}</option></select>\n </div>\n <div class='col-sm-12'> <hr> </div></div>",
          //popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Description</label>\n        <input type='text' ng-model=\"description\" class='form-control'/>\n    </div>\n   <div class=\"form-group\">\n        <label class='control-label'>Name</label>\n        <input type='text' ng-model=\"key\" class='form-control'/>\n    </div>\n   <div class=\"form-group\">\n        <label class='control-label'>Options</label>\n        <textarea class=\"form-control\" rows=\"3\" ng-model=\"optionsText\"/>\n    </div>\n\n    <hr/>\n    <div class='form-group'>\n        <input type='submit' ng-click=\"popover.save($event)\" class='btn btn-primary' value='Save'/>\n        <input type='button' ng-click=\"popover.cancel($event)\" class='btn btn-default' value='Cancel'/>\n        <input type='button' ng-click=\"popover.remove($event)\" class='btn btn-danger' value='Delete'/>\n    </div>\n</form>"
          popoverTemplateUrl: '/dealer/template/select.html'
        });
     
      /*<div  class=\"dropdown open\"> <ul class=\"dropdown-menu\"><li ng-repeat=\"value in options\" id=\"{{formName+index}}\"><a >{{value}}</a></li>    </ul> </div>
*/      
      /*template: "<div class=\"form-group col-sm-12\">\n    <label for=\"{{formName+index}}\" class=\"col-md-3 control-label\">{{label}}</label>\n    <div class=\"col-md-6\">\n        <select ng-options=\"value for value in options\" id=\"{{formName+index}}\" class=\"form-control\"\n            ng-model=\"inputText\" ng-init=\"inputText = options[0]\"/>\n        <p class='help-block'>{{description}}</p>\n    </div>\n <div class='col-sm-12'> <hr> </div></div>",
      <li><a href=\"#\">Another action</a></li>  <li><a href=\"#\">Something else here</a></li>  <li class=\"divider\"></li>  <li><a href=\"#\">Separated link</a></li>
          */
     
    }
    
  ]);

}).call(this);
