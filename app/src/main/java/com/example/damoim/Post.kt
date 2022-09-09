package com.example.damoim

class Post {
    /**
     * 글의 ID
     */
    var postId = ""
    /**
     * 글 작성자의 ID
     */
    var writerId = ""
//모임 지역
    var location = ""
    // 모임이름
    var groupName = ""
    //모임 목적
    var purposeMessage = ""
    //모임 메인이미지
    var moimImgUri = ""
    /**
     * 글이 쓰여진 시간
     */
    var writeTime: Any = Any()

    /**
     * 댓글의 개수
     */
    var commentCount = 0
}