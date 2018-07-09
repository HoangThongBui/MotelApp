'use strict'
const mongoose = require("mongoose");

const PostReportSchema = new mongoose.Schema({
  post_id: {
    type: mongoose.Schema.Types.ObjectId,
    ref: "Post"
  },
  report_time: {
    type: Date,
    default: Date.now()
  },
  report_status: Boolean
}, {
  collection: 'reports',
  max: 1000
});

module.exports = mongoose.model("PostReport", PostReportSchema);
