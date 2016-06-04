angular.module("tradeApp.controllers", [])
    .controller("MainCtrl", function ($rootScope, $scope, ExecutorService, OrderService) {
        const flashEffectTime = 5000
        $scope.executed = ExecutorService.executed();
        $scope.lastExecution = ExecutorService.lastExecuted();
        $scope.buyOrders = OrderService.buy();
        $scope.sellOrders = OrderService.sell();

        $scope.socket = {
            client: null,
            stomp: null
        };

        const delayExecutionNotification = (execution) =>
            setTimeout(() => {

                execution.new = true;
                const buy = $scope.buyOrders.find((el) => el.id === execution.buyOrder.id)
                const sell = $scope.sellOrders.find((el) => el.id === execution.sellOrder.id)
                if (!buy || !sell) {
                    delayExecutionNotification(execution)
                    return;
                }

                $rootScope.$apply(() => {
                    buy.executed = true;
                    buy.remain = execution.buyOrder.remain
                    sell.executed = true;
                    sell.remain = execution.sellOrder.remain
                    $scope.executed.push(execution);
                    if ($scope.executed.length > 10) {
                        $scope.executed.shift()
                    }
                })

                setTimeout(() => {
                    $rootScope.$apply(() => {
                        buy.executed = false;
                        sell.executed = false;
                        $scope.buyOrders = $scope.buyOrders.filter((el) => el.remain !== 0)
                        $scope.sellOrders = $scope.sellOrders.filter((el) => el.remain !== 0)
                    })
                    setTimeout(() => $rootScope.$apply(() => execution.new = false), 3000);
                }, 3000);
            }, 10000)


        const newBuyOrder = (message) => {
            const buyOrder = JSON.parse(message.body);
            buyOrder.new = true;
            setTimeout(() => $rootScope.$apply(() => buyOrder.new = false), 4000);
            $rootScope.$apply(() => $scope.buyOrders.push(buyOrder))
        };

        const newSellOrder = (message) => {
            const sellOrder = JSON.parse(message.body);
            sellOrder.new = true;
            setTimeout(() => $rootScope.$apply(() => sellOrder.new = false), 4000);
            $rootScope.$apply(() => $scope.sellOrders.push(sellOrder))
        };

        const newExecution = (message) => {
            const e = JSON.parse(message.body);
            delayExecutionNotification(e)
        };

        const initSockets = () => {
            $scope.socket.client = new SockJS('/ws');
            $scope.socket.stomp = Stomp.over($scope.socket.client);
            $scope.socket.stomp.connect({}, () => {
                $scope.socket.stomp.subscribe("/topic/newBuyOrder", newBuyOrder);
                $scope.socket.stomp.subscribe("/topic/newSellOrder", newSellOrder);
                $scope.socket.stomp.subscribe("/topic/newExecution", newExecution);
            });
            $scope.socket.client.onclose = () => setTimeout(initSockets, 10000);
        };

        initSockets()
    })
