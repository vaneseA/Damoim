package com.example.damoim.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.damoim.CreateActivity
import com.example.damoim.MoimDetailActivity
import com.example.damoim.Post

import com.example.damoim.R
import com.example.damoim.adapters.ViewPagerFragmentStateAdapter
import com.example.damoim.databinding.FragmentBottomTab1MeetBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_bottom_tab1_meet.*
import kotlinx.android.synthetic.main.layout_recycler_meet_base_item.view.*


class BottomTab1MeetFragment : BaseFragment() {
    // 글 목록을 저장하는 변수
    val posts: MutableList<Post> = mutableListOf()
    lateinit var binding: FragmentBottomTab1MeetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 1. View Binding 설정
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_tab1_meet, container, false)

        // 2. View Pager의 FragmentStateAdapter 설정
        binding.viewPager.adapter = activity?.let { ViewPagerFragmentStateAdapter(it) }

        // 3. View Pager의 Orientation 설정
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // 4. TabLayout + ViewPager2 연동 (ViewPager2에 Adapter 연동 후에)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()

        setupEvents()
        setValues()
        // 5. return Fragment Layout View
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 에 LayoutManager 설정
        val layoutManager = LinearLayoutManager(context)
        // 리사이클러뷰의 아이템을 역순으로 정렬하게 함
        layoutManager.reverseLayout = true
        // 리사이클려뷰의 아이템을 쌓는 순서를 끝부터 쌓게 함
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = MyAdapter()
        // Firebase 에서 Post 데이터를 가져온 후 posts 변수에 저장
        FirebaseDatabase.getInstance().getReference("/Posts")
            .orderByChild("writeTime").addChildEventListener(object : ChildEventListener {
                // 글이 추가된 경우
                override fun onChildAdded(snapshot: DataSnapshot, prevChildKey: String?) {
                    snapshot?.let { snapshot ->
                        // snapshop 의 데이터를 Post 객체로 가져옴
                        val post = snapshot.getValue(Post::class.java)
                        post?.let {
                            // 새 글이 마지막 부분에 추가된 경우
                            if (prevChildKey == null) {
                                //글 목록을 저장하는 변수에 post 객체 추가
                                posts.add(it)
                                // RecyclerView 의 adapter 에 글이 추가된 것을 알림
                                recyclerView.adapter?.notifyItemInserted(posts.size - 1)
                            } else {
                                // 글이 중간에 삽입된 경우 prevChildKey 로 한단계 앞의 데이터의 위치를 찾은 뒤 데이터를 추가한다.
                                val prevIndex = posts.map { it.postId }.indexOf(prevChildKey)
                                posts.add(prevIndex + 1, post)
                                // RecyclerView 의 adapter 에 글이 추가된 것을 알림
                                recyclerView.adapter?.notifyItemInserted(prevIndex + 1)
                            }
                        }
                    }
                }


                // 글이 변경된 경우
                override fun onChildChanged(snapshot: DataSnapshot, prevChildKey: String?) {
                    snapshot?.let { snapshot ->
                        // snapshop 의 데이터를 Post 객체로 가져옴
                        val post = snapshot.getValue(Post::class.java)
                        post?.let { post ->
                            // 글이 변경된 경우 글의 앞의 데이터 인덱스에 데이터를 변경한다.
                            val prevIndex = posts.map { it.postId }.indexOf(prevChildKey)
                            posts[prevIndex + 1] = post
                            recyclerView.adapter?.notifyItemChanged(prevIndex + 1)
                        }
                    }
                }

                // 글의 순서가 이동한 경우
                override fun onChildMoved(snapshot: DataSnapshot, prevChildKey: String?) {
                    // snapshot
                    snapshot?.let {
                        // snapshop 의 데이터를 Post 객체로 가져옴
                        val postsssss = snapshot.getValue(Post::class.java)
                        postsssss?.let { post ->
                            // 기존의 인덱스를 구한다
                            val existIndex = posts.map { it.postId }.indexOf(post.postId)
                            // 기존에 데이터를 지운다.
                            posts.removeAt(existIndex)
                            recyclerView.adapter?.notifyItemRemoved(existIndex)
                            // prevChildKey 가 없는 경우 맨마지막으로 이동 된 것
                            if (prevChildKey == null) {
                                posts.add(post)
                                recyclerView.adapter?.notifyItemChanged(posts.size - 1)
                            } else {
                                // prevChildKey 다음 글로 추가
                                val prevIndex = posts.map { it.postId }.indexOf(prevChildKey)
                                posts.add(prevIndex + 1, post)
                                recyclerView.adapter?.notifyItemChanged(prevIndex + 1)
                            }
                        }
                    }
                }

                // 글이 삭제된 경우
                override fun onChildRemoved(snapshot: DataSnapshot) {
                    snapshot?.let {
                        // snapshot 의 데이터를 Post 객체로 가져옴
                        val postsssss = snapshot.getValue(Post::class.java)
                        //
                        postsssss?.let { post ->
                            // 기존에 저장된 인덱스를 찾아서 해당 인덱스의 데이터를 삭제한다.
                            val existIndex = posts.map { it.postId }.indexOf(post.postId)
                            posts.removeAt(existIndex)
                            recyclerView.adapter?.notifyItemRemoved(existIndex)
                        }
                    }
                }

                // 취소된 경우
                override fun onCancelled(databaseError: DatabaseError) {
                    // 취소가 된경우 에러를 로그로 보여준다
                    databaseError?.toException()?.printStackTrace()
                }
            })


    }
    override fun setupEvents() {
        binding.meettingAddBtn.setOnClickListener {
            startActivity(Intent(mContext, CreateActivity::class.java))
        }
    }

    override fun setValues() {

    }

    // Tab & ViewPager 연동 및 Tab title 설정
    private fun getTabTitle(position: Int): String? {
        return when (position) {
            0 -> "전체"
            1 -> "아웃도어/   여행"
            2 -> "운동/스포츠"
            else -> "인문학/책/글"
        }
    }

    // RecyclerView 에서 사용하는 View 홀더 클래스
    inner class MyViewHodler(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //        // 글의 배경 이미지뷰
//        val imageView: ImageView = itemView.imageView
        // 모임 지역
        val tv_location_meet_basic_recycler_item: TextView =
            itemView.tv_location_meet_basic_recycler_item

        // 모임 이름
        val tv_meet_name_meet_basic_recycler_item: TextView =
            itemView.tv_meet_name_meet_basic_recycler_item

        // 모임 목적
        val tv_purpose_of_meet_meet_basic_recycler_item: TextView =
            itemView.tv_purpose_of_meet_meet_basic_recycler_item
//
        // 모임 이미지
        val circle_image_view_meet_basic_recycler_item: ImageView =
            itemView.circle_image_view_meet_basic_recycler_item
    }

    // RecyclerView 의 어댑터 클래스
    inner class MyAdapter : RecyclerView.Adapter<MyViewHodler>() {
        // RecyclerView 에서 각 Row(행)에서 그릴 ViewHolder 를 생성할때 불리는 메소드
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHodler {
            return MyViewHodler(
                LayoutInflater.from(context).inflate(
                    com.example.damoim.R.layout.layout_recycler_meet_base_item,
                    parent, false
                )
            )
        }

        // RecyclerView 에서 몇개의 행을 그릴지 기준이 되는 메소드
        override fun getItemCount(): Int {
            return posts.size
        }

        // 각 행의 포지션에서 그려야할 ViewHolder UI 에 데이터를 적용하는 메소드
        override fun onBindViewHolder(holder: MyViewHodler, position: Int) {
            val post = posts[position]

            // 모임 지역
            holder.tv_location_meet_basic_recycler_item.text = post.location
            // 모임 이름
            holder.tv_meet_name_meet_basic_recycler_item.text = post.groupName
            // 모임 목적
            holder.tv_purpose_of_meet_meet_basic_recycler_item.text = post.purposeMessage
//          // 모임 이미지
            Picasso.get().load(Uri.parse(post.moimImgUri)).fit().centerCrop().into(holder.circle_image_view_meet_basic_recycler_item)


            // 배경색깔
//            holder.containerView.setBackgroundColor(post.)

//             카드가 클릭되는 경우 DetailActivity 를 실행한다.
            holder.itemView.setOnClickListener {
                // 상세화면을 호출할 Intent 를 생성한다.
                val intent = Intent(context, MoimDetailActivity::class.java)
                // 선택된 카드의 ID 정보를 intent 에 추가한다.
                intent.putExtra("postId", post.postId)
                // intent 로 상세화면을 시작한다.
                startActivity(intent)
            }
        }
    }

}