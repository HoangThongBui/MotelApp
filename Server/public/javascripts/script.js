
function goToPage(page) {
    switch (page) {
        case 'home':
            location.href = '/admin/home';
            break;
        case 'user':
            location.href = '/admin/user';
            break;
        case 'post':
            location.href = '/admin/post';
            break;
        case 'post_report':
            location.href = '/admin/post_report';
            break;
    }
}