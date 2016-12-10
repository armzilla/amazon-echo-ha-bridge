angular.module('configurator', [])
    .service('bridgeService', ["$http", function ($http) {
        var self = this;
        this.state = {base: window.location.origin + "/api/devices", devices: [], error: ""};

        this.viewDevices = function () {
            this.state.error = "";
            return $http.get(this.state.base).then(
                function (response) {
                    self.state.devices = response.data[0].content;
                },
                function (error) {
                    if (error.data) {
                        self.state.error = error.data.message;
                    } else {
                        self.state.error = "If you're not seeing any devices, you may be running into problems with CORS. " +
                            "You can work around this by running a fresh launch of Chrome with the --disable-web-security flag.";
                    }
                    console.log(error);
                }
            );
        };

        this.addDevice = function (id, name, type, onUrl, offUrl) {
            this.state.error = "";
            if (id) {
                var putUrl = this.state.base + "/" + id;
                return $http.put(putUrl, {
                    id: id,
                    name: name,
                    deviceType: type,
                    onUrl: onUrl,
                    offUrl: offUrl
                }).then(
                    function (response) {
                        self.viewDevices();
                    },
                    function (error) {
                        if (error.data) {
                            self.state.error = error.data.message;
                        }
                        console.log(error);
                    }
                );
            } else {
                return $http.post(this.state.base, {
                    name: name,
                    deviceType: type,
                    onUrl: onUrl,
                    offUrl: offUrl
                }).then(
                    function (response) {
                        self.viewDevices();
                    },
                    function (error) {
                        if (error.data) {
                            self.state.error = error.data.message;
                        }
                        console.log(error);
                    }
                );
            }
        };

        this.deleteDevice = function (id) {
            this.state.error = "";
            return $http.delete(this.state.base + "/" + id).then(
                function (response) {
                    self.viewDevices();
                },
                function (error) {
                    if (error.data) {
                        self.state.error = error.data.message;
                    }
                    console.log(error);
                }
            );
        };

        this.editDevice = function (id, name, type, onUrl, offUrl) {
            this.device.id = id;
            this.device.name = name;
            this.device.onUrl = onUrl;
            this.device.offUrl = offUrl;
        };
    }])

    .controller('ViewingController', ["$scope", "bridgeService", function ($scope, bridgeService) {
        bridgeService.viewDevices();
        $scope.bridge = bridgeService.state;
        $scope.deleteDevice = function (device) {
            bridgeService.deleteDevice(device.id);
        };
        $scope.testUrl = function (url) {
            window.open(url, "_blank");
        };
        $scope.setBridgeUrl = function (url) {
            bridgeService.state.base = url;
            bridgeService.viewDevices();
        };
        $scope.editDevice = function (device) {
            bridgeService.editDevice(device.id, device.name, device.type, device.onUrl, device.offUrl);
        };
    }])

    .controller('AddingController', ["$scope", "bridgeService", function ($scope, bridgeService) {

        $scope.bridge = bridgeService.state;
        $scope.device = {id: "", name: "", type: "switch", onUrl: "", offUrl: ""};
        $scope.hubs = {
            vera: {
                base: ""
                , port: "3480"
                , id: ""
                , onUrl: "/data_request?id=action&output_format=json&serviceId=urn:upnp-org:serviceId:SwitchPower1&action=SetTarget&newTargetValue=1&DeviceNum={deviceId}"
                , offUrl: "/data_request?id=action&output_format=json&serviceId=urn:upnp-org:serviceId:SwitchPower1&action=SetTarget&newTargetValue=0&DeviceNum={deviceId}"
                , user: ""
                , pass: ""
            }
            , openhab: {
                base: ""
                , port: "8080"
                , id: ""
                , onUrl: "/CMD?{deviceId}=ON"
                , offUrl: "/CMD?{deviceId}=OFF"
                , user: ""
                , pass: ""
            }
        }
        // set default system to Vera
        $scope.selectedHub = "vera";
        $scope.system = $scope.hubs.vera;
        bridgeService.device = $scope.device;

        $scope.selectHub = function (hubName) {
            console.log('Selecting hub', $scope.selectedHub)
            if (hubName === "openhab") {
                $scope.system = $scope.hubs.openhab;
            }
            else {
                $scope.system = $scope.hubs.vera;
            }
        }

        $scope.buildUrls = function () {
            var base = ""
            if ($scope.system.base.indexOf("http") < 0) {
                base = "http://";
            }
            base += $scope.system.base;

            var user = $scope.system.user.trim();
            var pass = $scope.system.pass.trim();
            var creds = "";
            if (user.length > 0 && pass.length > 0) {
                creds = encodeURIComponent(user) + ":" + encodeURIComponent(pass) + "@";
            }

            var index = base.indexOf("//") + 2;
            base = base.slice(0, index) + creds + base.slice(index);

            $scope.device.onUrl = base + ":" + $scope.system.port + $scope.system.onUrl.replace("{deviceId}", $scope.system.id);
            $scope.device.offUrl = base + ":" + $scope.system.port + $scope.system.offUrl.replace("{deviceId}", $scope.system.id);
      };

        $scope.testUrl = function (url) {
            window.open(url, "_blank");
        };

        $scope.addDevice = function () {
            bridgeService.addDevice($scope.device.id, $scope.device.name, $scope.device.type, $scope.device.onUrl, $scope.device.offUrl).then(
                function () {
                    $scope.device.id = "";
                    $scope.device.name = "";
                    $scope.device.onUrl = "";
                    $scope.device.offUrl = "";
                },
                function (error) {
                }
            );
        }
    }])

    .controller('ErrorsController', ["$scope", "bridgeService", function ($scope, bridgeService) {
        $scope.bridge = bridgeService.state;
    }]);