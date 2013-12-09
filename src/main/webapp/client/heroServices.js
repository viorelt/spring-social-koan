angular.module('heroservices', ['ngResource'])
    .factory('HttpSpinnerInterceptor', function ($q, $window) {
        return function (promise) {
            return promise.then(function (response) {

                return response;
            }, function (response) {

                if(response.status==0){
                    alert('Sesiunea a expirat. Veti fi redirectat in pagina de start');
                    window.location= appContext;

                }else{
                    alert('A intervenit o exceptie.')
                }

                return $q.reject(response);
            });
        }
    })
    .factory('FacebookService',function ($resource) {
        return $resource(appContext + '/facebook', {}, {
            index: { method: 'GET', isArray: true}
        });
    });