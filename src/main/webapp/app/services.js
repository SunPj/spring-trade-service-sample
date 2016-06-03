angular.module("tradeApp.services", ["ngResource"])
    .factory("ExecutorService", ($resource) => {
        return $resource("/api/order/executed/:action", {}, {
            executed: {method: 'GET', params: {action: ''}, isArray: true},
            lastExecuted: {method: 'GET', params: {action: 'last'}}
        });
    })
    .factory("OrderService", ($resource) => {
        return $resource("/api/order/waiting/:action", {}, {
            sell: {method: 'GET', params: {action: 'sell'}, isArray: true},
            buy: {method: 'GET', params: {action: 'buy'}, isArray: true}
        });
    });