'use striict';
const mongoose = require('mongoose');
const Post = mongoose.model('Post');
const Room = mongoose.model('Room');
const User = mongoose.model('User');
const PostReport = mongoose.model('PostReport');
const address = require('../../custom_modules/host');

exports.get_post_detail = async (req, res) => {
    try {
        var post = await Post.findById(req.params.post_id);
        post.room = await Room.findById(post.room);
        post.user = await User.findById(post.user);
        res.render('post_detail', {post, page_type : "detail", address: address.web_server});
    } catch (error) {
        console.log(error);
    }
}

exports.confirm_post = async (req,res) => {
    try {
        var currentStatus = (await Post.findById(req.params.post_id)).status;
        var confirmSet = {
            status : "c"
        };
        await Post.findByIdAndUpdate(req.params.post_id, {"$set" : confirmSet});
        var report_time = new Date();
        var newReport = new PostReport({
            post : req.params.post_id,
            user : req.body.admin_id,
            from : currentStatus,
            to : "c",
            description: "This post is confirmed!",
            report_time
        });
        await newReport.save();
        res.redirect('/admin/post/' + req.params.post_id);
    } catch (error) {
        console.log(error);
    }
}

exports.reject_post = async (req,res) => {
    try {
        var currentStatus = (await Post.findById(req.params.post_id)).status;
        var rejectSet = {
            status : "r"
        };
        await Post.findByIdAndUpdate(req.params.post_id, {"$set" : rejectSet});
        var report_time = new Date();
        var newReport = new PostReport({
            post : req.params.post_id,
            user : req.body.admin_id,
            from : currentStatus,
            to : "r",
            description: "This post is rejected!",
            report_time
        });
        await newReport.save();
        res.redirect('/admin/post/' + req.params.post_id);
    } catch (error) {
        console.log(error);
    }
}

exports.delete_post = async (req,res) => {
    try {
        var currentStatus = (await Post.findById(req.params.post_id)).status;
        var deleteSet = {
            status : "d"
        };
        await Post.findByIdAndUpdate(req.params.post_id, {"$set" : deleteSet});
        var report_time = new Date();
        var newReport = new PostReport({
            post : req.params.post_id,
            user : req.body.admin_id,
            from : currentStatus,
            to : "d",
            description: "This post is deleted!",
            report_time
        });
        await newReport.save();
        res.redirect('/admin/post_report');
    } catch (error) {
        console.log(error);
    }
}
