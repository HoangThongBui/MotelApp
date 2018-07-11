'use strict'

const UserController = require('../controllers/UserController');

module.exports = function(app){
    app.route('/user/api/check_user_status/:id')
        .get(UserController.check_user_status);

    app.route('/user/api/login')
        .post(UserController.user_login);

    app.route('/user/api/register')
        .post(UserController.user_register);

    app.route('/user/api/get_user_by_id/:id')
        .get(UserController.get_user_by_id);    
}