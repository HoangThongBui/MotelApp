'use striict';
const mongoose = require('mongoose');
const Post = mongoose.model('Post');
const Room = mongoose.model('Room');
const User = mongoose.model('User');

exports.get_post_detail = async (req, res) => {
    try {
        var post = await Post.findById(req.params.post_id);
        post.room = await Room.findById(post.room);
        post.user = await User.findById(post.user);
        res.render('post_detail', {post, page_type : "detail"});
    } catch (error) {
        console.log(error);
    }
}
