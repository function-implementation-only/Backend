package com.example.speedsideproject.websocket.service;

import com.example.speedsideproject.account.repository.AccountRepository;
import com.example.speedsideproject.error.CustomException;
import com.example.speedsideproject.error.ErrorCode;
import com.example.speedsideproject.global.dto.GlobalResponseDto;
import com.example.speedsideproject.post.Post;
import com.example.speedsideproject.post.PostRepository;
import com.example.speedsideproject.security.user.UserDetailsImpl;
import com.example.speedsideproject.websocket.domain.Room;
import com.example.speedsideproject.websocket.dto.RoomReqDto;
import com.example.speedsideproject.websocket.dto.RoomResDto;
import com.example.speedsideproject.websocket.repository.ChatRepository;
import com.example.speedsideproject.websocket.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final ChatRepository chatRepository;
    private final PostRepository postRepository;
    private final AccountRepository accountRepository;

    public GlobalResponseDto<?> joinRoom( Long roomId, UserDetailsImpl userDetails) {

        Room room = roomRepository.findById(roomId).orElseThrow(
                ()-> new CustomException(ErrorCode.NotfoundRoom)
        );
        Post post = postRepository.findById(room.getPost().getId()).orElseThrow(
                ()-> new CustomException(ErrorCode.NotFoundPost)
        );

        RoomResDto roomResponseDto = new RoomResDto(room,post);
        return GlobalResponseDto.success(roomResponseDto,room.getId()+"번방");
    }

    //방 만들기
    public GlobalResponseDto<?> createRoom(RoomReqDto roomReqDto, UserDetailsImpl userDetails) {
        Post post = postRepository.findById(roomReqDto.getPostId()).orElseThrow(
                ()-> new CustomException(ErrorCode.NotFoundPost)
        );

        //이미 만든방이 있다면 room에 저장후 리턴
        Room room = roomRepository.findRoomByJoinUser_IdAndPostUser_IdAndPostId(userDetails.getAccount().getId(), post.getAccount().getId(), roomReqDto.getPostId())
                //만들어진 방이 없다면 새로 만들어서 리턴
                .orElse(new Room(post.getAccount(), userDetails.getAccount(), post));

        roomRepository.save(room);
        RoomResDto roomResponseDto = new RoomResDto(room);
        return GlobalResponseDto.success(roomResponseDto,null);
    }

    public GlobalResponseDto<?> roomList(UserDetailsImpl userDetails){
        List<Room> roomList = roomRepository.findAllByJoinUser_IdOrPostUser_IdOrderByIdDesc(userDetails.getAccount().getId(),userDetails.getAccount().getId());
        List<RoomResDto> roomResponseDtos = new ArrayList<>();

        for(Room room : roomList ){
            roomResponseDtos.add(new RoomResDto(room));
        }

        if(roomList.isEmpty()){
            return GlobalResponseDto.fail("채팅 내역이 없습니다");
        }else{
            return GlobalResponseDto.success(roomResponseDtos,null);
        }

    }
}
