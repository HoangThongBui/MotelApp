'use strict'

const mongoose = require('mongoose');
const User = mongoose.model('User');
const Post = mongoose.model('Post');
const PostReport = mongoose.model('PostReport');

exports.go_to_post_report_page = async (req, res) => {
    try {
        var reports = await PostReport.find().sort({ report_time: -1 });
        for (var i = 0; i < reports.length; i++) {
            reports[i].post = await Post.findById(reports[i].post);
            reports[i].user = await User.findById(reports[i].user);
        }
    } catch (error) {
        console.log(error);
    }
    res.render('post_report', { reports, page_type: 'post_report' });
}