'use strict'

const mongoose = require('mongoose');
const Comment = mongoose.model('Comment');
const User = mongoose.model('User');

exports.get_comments = async function (req, res) {
    try {
        var post_id = req.params.post_id;
        var comments = await Comment.find({ post: post_id }).sort({ comment_time: -1 });
        for (var i = 0; i < comments.length; i++) {
            comments[i].user = await User.findById(comments[i].user, {password: 0});
        }
        // await waitTimeOut();
        res.json(comments);
    } catch (error) {
        res.send(error)
    }
}

//server waiting simulation
function waitTimeOut(){
    return new Promise((res,rej)=> {setTimeout(res, 3000)});
}