const multer = require('multer');
const path = require('path');

const multerConfig = {
    storage: multer.diskStorage({
        destination: (req, file, next) => {
            // var imagePath = path.dirname(__dirname);
            var imagePath = '';
            if (req.params.user_id) {
                imagePath += '/public/images/users/';
            }
            if (req.body.title) {
                imagePath += '/public/images/posts/';
            }
            next(null, path.dirname(__dirname) + imagePath);
        },
        filename: (req, file, next) => {
            //split 2 situations
            if (req.params.user_id) {
                const extension = file.originalname.split('.')[1]//get extension
                next(null, req.params.user_id + '_' + file.fieldname + '_' + new Date().getTime() + '.' + extension); //custom file name
            }
            else {
                const extension = file.originalname.split('.')[1]//get extension
                console.log(file);
                next(null, 'rooms_image' + '_' + new Date().getTime() + '.' + extension); //custom file name
            }
        }
    })
};

module.exports = multer(multerConfig); //export custom multer module