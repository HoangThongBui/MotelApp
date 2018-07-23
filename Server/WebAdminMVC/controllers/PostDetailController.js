'use strict';
const mongoose = require('mongoose');
const Post = mongoose.model('Post');
const Room = mongoose.model('Room');
const User = mongoose.model('User');
const PostReport = mongoose.model('PostReport');
const Comment = mongoose.model('Comment');
var post;


exports.get_post_detail = async (req, res) => {
    try {
        var post = await Post.findById(req.params.post_id);
        post.user = await User.findById(post.user);
        post.room = await Room.findById(post.room);
        if (post.status === 'c') {
            var comments = await Comment.find({ "post": req.params.post_id });
            for (var i = 0; i < comments.length; i++) {
                comments[i].user = await User.findById(comments[i].user, { status: 0, role: 0, password: 0 });
            }
            res.render('post_detail', {post, comments});
        }
        else {
            res.render('post_detail', {post});
        }
    } catch (error) {
        console.log(error);
    }
}

exports.confirm_post = async (req, res) => {
    try {
        var currentStatus = (await Post.findById(req.params.post_id)).status;
        var confirmSet = {
            status: "c"
        };
        await Post.findByIdAndUpdate(req.params.post_id, { "$set": confirmSet });
        var report_time = new Date();
        var newReport = new PostReport({
            post: req.params.post_id,
            user: req.body.admin_id,
            from: currentStatus,
            to: "c",
            description: "This post is confirmed!",
            report_time
        });
        await newReport.save();
        res.redirect('/admin/post/' + req.params.post_id);
    } catch (error) {
        console.log(error);
    }
}

exports.reject_post = async (req, res) => {
    try {
        var currentStatus = (await Post.findById(req.params.post_id)).status;
        var rejectSet = {
            status: "r"
        };
        await Post.findByIdAndUpdate(req.params.post_id, { "$set": rejectSet });
        var report_time = new Date();
        var newReport = new PostReport({
            post: req.params.post_id,
            user: req.body.admin_id,
            from: currentStatus,
            to: "r",
            description: "This post is rejected!",
            report_time
        });
        await newReport.save();
        res.redirect('/admin/post/' + req.params.post_id);
    } catch (error) {
        console.log(error);
    }
}

exports.delete_post = async (req, res) => {
    try {
        var currentStatus = (await Post.findById(req.params.post_id)).status;
        var deleteSet = {
            status: "d"
        };
        await Post.findByIdAndUpdate(req.params.post_id, { "$set": deleteSet });
        var report_time = new Date();
        var newReport = new PostReport({
            post: req.params.post_id,
            user: req.body.admin_id,
            from: currentStatus,
            to: "d",
            description: "This post is deleted!",
            report_time
        });
        await newReport.save();
        res.redirect('/admin/post_report');
    } catch (error) {
        console.log(error);
    }
}

exports.delete_comment = async (req,res) => {
    try {
        await Comment.findByIdAndRemove(req.params.comment_id);
        res.redirect('/admin/post/' + req.params.post_id);   
    } catch (error) {
        console.log(error);
    }
}
