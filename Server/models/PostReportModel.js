'use strict'
const mongoose = require("mongoose");

const PostReportSchema = new mongoose.Schema({
  post: {
    type: mongoose.Schema.Types.ObjectId,
    ref: "Post"
  },
  user: {
    type: mongoose.Schema.Types.ObjectId,
    ref: "User"
  },
  from: String,
  to: String,
  description : String,
  report_time: {
    type: Date
  },
}, {
    collection: 'post_reports',
    max: 100000
  });

module.exports = mongoose.model("PostReport", PostReportSchema);
