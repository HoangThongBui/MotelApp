'use strict'

const AdminController = require('../controllers/AdminController');
const Multer = require('../../custom_modules/multer');

module.exports = (app) => {
    app.route('/admin/change_password')
        .get(AdminController.go_to_confirm_current_password);

    app.route('/admin/change_password/confirm_current_password')
        .post(AdminController.confirm_current_password);

    app.route('/admin/change_password/create_new_password')
        .post(AdminController.create_new_password);

    app.route('/admin/edit_profile')
        .get(AdminController.go_to_edit_profile);

    app.route('/admin/edit_profile/update_profile')
        .post(Multer.single('avatar'), AdminController.update_profile);
}