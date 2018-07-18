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
        //only get comments of active user
        var activeComments = [];
        for (var i = 0; i < comments.length; i++){
            if (comments[i].user.status){
                activeComments.push(comments[i]);
            }
        }
        // await waitTimeOut();
        res.json(activeComments);
    } catch (error) {
        res.send(error)
    }
}

exports.post_a_comment = async function (req,res) {
    try {
        var newComment = new Comment({
            post: req.body.post_id,
            user: req.body.user_id,
            detail: req.body.detail,
            comment_time: new Date()
        })
        await newComment.save();
        res.send('Comment posted!');
    } catch (error) {
        console.log(error);
        res.send("Server internal error!");
    }
}

exports.delete_comment = async (req,res) => {
    try {
        await Comment.findByIdAndRemove(req.params.comment_id);
        res.send('Comment deleted!');
    } catch (error) {
        res.send(error);
        console.log(error);
    }
}

//server waiting simulation
function waitTimeOut(){
    return new Promise((res,rej)=> {setTimeout(res, 3000)});
}