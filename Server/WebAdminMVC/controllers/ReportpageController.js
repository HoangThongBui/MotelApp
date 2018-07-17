'use strict'

exports.go_to_post_report_page = (req,res) => {
    res.render('post_report', {page_type : 'post_report'});
}