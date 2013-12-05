jQuery.browser = {};
var currentFlowIdx=0;
jQuery.browser.msie = jQuery.support.boxModel;
$.ajaxSetup({async:false});
angular.module('herro', ['herroservices']).config(function ($routeProvider) {
    $routeProvider.

        when('/facebook', {templateUrl:appContext+'/views/facebook/home.html', controller:FacebookController})
        .when('/twiter', {templateUrl:appContext+'/views/twiter/home.html', controller:TwiterController})
        .when('/linkedin', {templateUrl:appContext+'/views/linkedin/home.html', controller:LinkedInController})
        .when('/googleplus', {templateUrl:appContext+'/views/googleplus/home.html', controller:GooglePlusController})

}).config(['$httpProvider', function ($httpProvider) {
        $httpProvider.responseInterceptors.push('HttpSpinnerInterceptor');
        $httpProvider.defaults.transformRequest.push(function (data, headersGetter) {

            return data;
        });
    }]).run(function ($rootScope, $route) {
        $rootScope.layoutPartial = function (partialName) {
            return $route.current[partialName]
        };
    });