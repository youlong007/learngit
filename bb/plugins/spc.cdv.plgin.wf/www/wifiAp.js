var exec = require('cordova/exec');

exports.wfAP = function(flg,user,pw,ecpt, success, error) {
    exec(success, error, "wifiAp", "wfAP", [flg,user,pw,ecpt]);
};
