'use strict'

exports.getPassedTime = function (postTime) {
    var timeUnit;
    var passedTime = parseInt((new Date().getTime() - postTime) / 1000);
    if (passedTime < 60) {
        return "Cách đây không lâu";
    }
    if (passedTime < 3600) {
        timeUnit = parseInt(passedTime / 60);
        return "Khoảng " + timeUnit + " phút trước";
    }
    if (passedTime < 86400) {
        timeUnit = parseInt(passedTime / 3600);
        return "Khoảng " + timeUnit + " giờ trước";
    }
    if (passedTime < 604800) {
        timeUnit = parseInt(passedTime / 86400);
        return "Khoảng " + timeUnit + " ngày trước";
    }
    if (passedTime < 2419200) {
        timeUnit = parseInt(passedTime / 604800);
        return "Khoảng " + timeUnit + " tuần trước";
    }
    if (passedTime < 29030400) {
        timeUnit = parseInt(passedTime / 2419200);
        return "Khoảng " + timeUnit + " tháng trước";
    }
    return "Cách đây rất lâu";
}
