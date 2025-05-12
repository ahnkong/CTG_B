// package com.ctg.backend.service;

// import java.io.IOException;
// import java.time.LocalDateTime;
// import java.util.List;
// import java.util.Optional;
// import java.util.stream.Collectors;
// import java.util.stream.Stream;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.Sort;
// import org.springframework.data.jpa.repository.Modifying;
// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
// import org.springframework.web.multipart.MultipartFile;

// import com.ctg.backend.dto.BoardDTO;
// import com.ctg.backend.dto.ImageDTO;
// import com.ctg.backend.entity.Board;
// import com.ctg.backend.entity.BoardType;
// import com.ctg.backend.entity.Like;
// import com.ctg.backend.entity.User;
// import com.ctg.backend.repository.BoardRepository;
// import com.ctg.backend.repository.CommentRepository;
// import com.ctg.backend.repository.LikeRepository;
// import com.ctg.backend.repository.ReCommentRepository;
// import com.ctg.backend.repository.UserRepository;

// @Service
// public class BoardService {
    
//     @Autowired
//     private BoardRepository boardRepository;

//     @Autowired
//     private LikeRepository likeRepository;
    
//     @Autowired
//     private ImageService imageService;

//     @Autowired
//     private CommentRepository commentRepository;

//     @Autowired
//     private ReCommentRepository reCommentRepository;

//     @Autowired
//     private UserRepository userRepository;

//     public BoardService(BoardRepository boardRepository, ImageService imageService, LikeRepository likeRepository, CommentRepository commentRepository, ReCommentRepository reCommentRepository, UserRepository userRepository) {
//         this.boardRepository = boardRepository;
//         this.imageService = imageService;
//         this.likeRepository = likeRepository;
//         this.commentRepository = commentRepository;
//         this.reCommentRepository = reCommentRepository;
//         this.userRepository = userRepository;
//     }

//     // BoardType 유효성 검사 메서드
//     // 기존 private -> public으로 변경
//     public void validateBoardType(String type) {
//         try {
//             BoardType.valueOf(type); // Enum 값 확인
//         } catch (IllegalArgumentException e) {
//             throw new IllegalArgumentException("유효하지 않은 BoardType입니다: " + type);
//         }
//     }

//     // 모든 게시글 반환 (DTO 변환 포함)
//     public Page<BoardDTO> getAllBoards(Pageable pageable) {
//         return boardRepository.findAll(pageable).map(this::convertToDTO);
//     }

//     // 특정 게시글 반환 (DTO 변환 포함)
//     public BoardDTO getBoardById(Long boardId) {
//         Board board = boardRepository.findById(boardId)
//                 .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다: " + boardId));

//         // 게시글 작성자의 프로필 이미지 가져오기
//         User user = userRepository.findById(board.getUserId())
//         .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + board.getUserId()));
        
//         BoardDTO boardDTO = convertToDTO(board);
//         boardDTO.setUserProfileImage(user.getProfileImage()); // ✅ DTO에 프로필 이미지 추가
//         boardDTO.setUserNickname(user.getNickname());

//         return boardDTO;
//     }

//     // 검색 및 정렬된 게시글 반환
//     // 검색 + 타입 + 날짜 필터 + 정렬을 한 번에 처리
//     // @Transactional(readOnly = true)
//     // public Page<BoardDTO> getBoards(String search, Integer filterDate, String sort, String type, Pageable pageable) {
//     //     Sort sorting = Sort.by(Sort.Direction.DESC, "cDate");
//     //     pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sorting);
    
//     //     Page<Board> boards;
    
//     //     // 좋아요 순 정렬 (1주일 또는 1달 단위)
//     //     if ("likesLast7Days".equals(sort) || "likesLast30Days".equals(sort)) {
//     //         boards = boardRepository.findBoardsOrderByLikeCount(
//     //                 BoardType.valueOf(type), 
//     //                 filterDate, 
//     //                 pageable
//     //         );
//     //     } 
//     //     // 최신순 정렬
//     //     else if (search != null && !search.isEmpty()) {
//     //         boards = boardRepository.findByTitleContainingAndType(search, BoardType.valueOf(type), pageable);
//     //     } 
//     //     // 조회수 기준 정렬
//     //     else {
//     //         boards = boardRepository.findByTypeOrderByViewDesc(BoardType.valueOf(type), pageable);
//     //     }
    
//     //     // Board 엔티티를 BoardDTO로 변환하여 반환
//     //     return boards.map(this::convertToDTO);
//     // }
//         @Transactional(readOnly = true)
//         public Page<BoardDTO> getBoards(String search, Integer filterDate, String sort, String type, Pageable pageable) {
//             Page<Board> boards;

//             // ✅ 1. 인기글: 좋아요 기준 정렬
//             if ("likesLast7Days".equals(sort) || "likesLast30Days".equals(sort)) {
//                 // ✅ Pageable에서 정렬 제거 (JPQL에서 이미 ORDER BY 사용 중이므로)
//                 Pageable unsortedPageable = PageRequest.of(
//                     pageable.getPageNumber(),
//                     pageable.getPageSize(),
//                     Sort.unsorted()
//                 );
            
//                 boards = boardRepository.findBoardsOrderByLikeCount(
//                     BoardType.valueOf(type),
//                     filterDate,
//                     unsortedPageable
//                 );
            
//                 System.out.println("🔥 인기글 정렬 로직 실행됨");

//             } else {
//                 // ✅ 2. 오늘의 게시글: 최신순 정렬 적용
//                 Pageable sortedPageable = PageRequest.of(
//                     pageable.getPageNumber(),
//                     pageable.getPageSize(),
//                     Sort.by(Sort.Direction.DESC, "cDate") // Board 엔티티의 cDate 기준
//                 );
//                 boardRepository.findAllByType(BoardType.valueOf(type), sortedPageable);
                

//                 // ✅ 2-1. 검색어가 있는 경우 (검색 + 최신순 정렬)
//                 if (search != null && !search.isEmpty()) {
//                     boards = boardRepository.findByTitleContainingAndType(
//                             search,
//                             BoardType.valueOf(type),
//                             sortedPageable);

//                 // ✅ 2-2. 일반 최신글 (검색 없이 최신순)
//                 } else {
//                     boards = boardRepository.findAllByType(
//                             BoardType.valueOf(type),
//                             sortedPageable);
//                 }
//             }

//             return boards.map(this::convertToDTO);
//         }


//     // 게시글 생성
//     public BoardDTO createBoard(BoardDTO boardDTO, List<MultipartFile> images) throws IOException {
//         validateBoardType(boardDTO.getType().toString()); // BoardType 유효성 검사
        
//         // 해시태그 처리
//         String processedHashTags = processHashTags(boardDTO.getHashTag());
//         boardDTO.setHashTag(processedHashTags);

//         Board board = convertToEntity(boardDTO);
        
//         board.setBoardId(null);
//         board.setCDate(LocalDateTime.now());
//         board.setView(0);
//         board.setHidden(false);
//         board.setStatus("PUBLISHED");

//         Board savedBoard = boardRepository.save(board);

//         if (images != null && !images.isEmpty()) {
//             imageService.saveImages(images, savedBoard.getBoardId());
//         }

//         return convertToDTO(savedBoard);
//     }

    
//     //게시글 수정
//     public BoardDTO updateBoard(Long boardId, BoardDTO boardDTO, List<MultipartFile> newImages) {
//         validateBoardType(boardDTO.getType().toString()); // BoardType 유효성 검사 추가
        
//         // 해시태그 처리
//         String processedHashTags = processHashTags(boardDTO.getHashTag());
//         boardDTO.setHashTag(processedHashTags);

//         // 기존 게시글 조회
//         Board board = boardRepository.findById(boardId)
//                 .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다: " + boardId));

//         // 게시글 정보 업데이트
//         board.setTitle(boardDTO.getTitle());
//         board.setContent(boardDTO.getContent());
//         board.setHashTag(boardDTO.getHashTag());
//         board.setHidden(boardDTO.getHidden());
//         board.setType(boardDTO.getType());

//         // 기존 이미지를 삭제하고 새 이미지를 저장
//         if (newImages != null && !newImages.isEmpty()) {
//             try {
//                 // 기존 이미지 삭제
//                 imageService.deleteImagesByBoardId(boardId);
    
//                 // 새 이미지 저장
//                 imageService.saveImages(newImages, board.getBoardId());
                
//             } catch (IOException e) {
//                 throw new RuntimeException("이미지 저장 중 오류가 발생했습니다: " + e.getMessage(), e);
//             }
//         }

//         // 게시글 저장
//         Board updatedBoard = boardRepository.save(board);

//         return convertToDTO(updatedBoard);
//     }

//     // 게시글 삭제
//     @Transactional
//     public void deleteBoard(Long boardId) {
//         // 게시글 존재 여부 확인
//         Board board = boardRepository.findById(boardId)
//                 .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다: " + boardId));
    
//         // 게시글에 연결된 이미지 삭제
//         imageService.deleteImagesByBoardId(boardId);

//         // 게시글에 연결된 좋아요 삭제
//         likeRepository.deleteByBoard_BoardId(boardId);
    
//         // 게시글에 연결된 댓글 삭제
//         commentRepository.findByBoard_BoardId(boardId).forEach(comment -> {
//             // 댓글에 연결된 대댓글 삭제
//             reCommentRepository.deleteByComment_CommentId(comment.getCommentId());
//             // 댓글 삭제
//             commentRepository.deleteById(comment.getCommentId());
//         });
    
//         // 게시글 삭제
//         boardRepository.delete(board);
//     }

//     // 검색어가 있을 때 검색어가 포함된 게시글 반환
//     public Page<BoardDTO> searchBoards(String search, Pageable pageable) {
//         return boardRepository.findByTitleContainingOrContentContaining(search, search, pageable)
//                 .map(this::convertToDTO);
//     }

//     // 조회수 증가
//     @Transactional
//     @Modifying
//     public void incrementViews(Long boardId) {
//         boardRepository.incrementViewCount(boardId);
//         boardRepository.flush(); // 변경 사항을 즉시 DB에 반영
//     }

//     /*
//      * 임시저장 관련 기능
//      */

//     // 1. 임시 저장 생성/업데이트
//     @Transactional
//     public BoardDTO saveDraft(BoardDTO boardDTO, List<MultipartFile> images) throws IOException {
//         Optional<Board> existingDraft = boardRepository.findByUserIdAndStatus(boardDTO.getUserId(), "DRAFT");

//         Board board;
//         if (existingDraft.isPresent()) {
//             // 기존 임시 저장 데이터 업데이트
//             board = existingDraft.get();
//             board.setTitle(boardDTO.getTitle());
//             board.setContent(boardDTO.getContent());
//             board.setLastSavedAt(LocalDateTime.now());

//             // 이미지 처리
//             if (images != null && !images.isEmpty()) {
//                 imageService.deleteImagesByBoardId(board.getBoardId()); // 기존 이미지 삭제
//                 imageService.saveImages(images, board.getBoardId());    // 새 이미지 저장
//             }
//         } else {
//             // 새로운 DRAFT 생성
//             board = convertToEntity(boardDTO);
//             board.setStatus("DRAFT");
//             board.setLastSavedAt(LocalDateTime.now());
//             Board savedBoard = boardRepository.save(board);

//             // 이미지 저장
//             if (images != null && !images.isEmpty()) {
//                 imageService.saveImages(images, savedBoard.getBoardId());
//             }
//         }
//         return convertToDTO(board);
//     }

//     // 2. 임시 저장 불러오기
//     public BoardDTO getDraft(String userId) {
//         Board draft = boardRepository.findByUserIdAndStatus(userId, "DRAFT")
//                 .orElseThrow(() -> new RuntimeException("임시 저장된 게시글이 없습니다."));
//         return convertToDTO(draft);
//     }

//     // 3. 게시로 전환
//     @Transactional
//     public BoardDTO publishDraft(Long boardId) {
//         Board draft = boardRepository.findById(boardId)
//                 .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

//         if (!"DRAFT".equals(draft.getStatus())) {
//             throw new IllegalStateException("임시 저장된 게시글만 게시로 전환할 수 있습니다.");
//         }

//         draft.setStatus("PUBLISHED");
//         draft.setLastSavedAt(LocalDateTime.now());

//         Board publishedBoard = boardRepository.save(draft);
//         return convertToDTO(publishedBoard);
//     }

//     // 4. 오래된 임시 저장 삭제 (정리 작업)
//     @Scheduled(cron = "0 0 3 * * ?") // 매일 새벽 3시 실행
//     @Transactional
//     public void cleanupOldDrafts() {
//         LocalDateTime threshold = LocalDateTime.now().minusDays(30);

//         // 오래된 DRAFT 데이터 조회
//         List<Board> oldDrafts = boardRepository.findAllByStatusAndLastSavedAtBefore("DRAFT", threshold);

//         // 관련 이미지 삭제
//         for (Board draft : oldDrafts) {
//             imageService.deleteImagesByBoardId(draft.getBoardId());
//         }

//         // 오래된 DRAFT 데이터 삭제
//         boardRepository.deleteAll(oldDrafts);
//     }

//     /* 해시태그 */
//     // 태그 유효성 검사 및 처리 메서드
//     private String processHashTags(String hashTagInput) {
//         if (hashTagInput == null || hashTagInput.isEmpty()) {
//             return null;
//         }

//         // 해시태그를 쉼표와 공백으로 분리
//         String[] tags = hashTagInput.split("[,\\s]+");

//         // 태그 개수 제한
//         if (tags.length > 5) {
//             throw new IllegalArgumentException("해시태그는 최대 5개까지 입력 가능합니다.");
//         }

//         // 각 태그에서 # 및 길이 검증
//         for (int i = 0; i < tags.length; i++) {
//             String tag = tags[i].trim();
    
//             // 모든 태그는 #으로 시작해야 함
//             if (!tag.startsWith("#")) {
//                 tag = "#" + tag;
//             }
    
//             // 해시태그 길이 제한 (최대 30자)
//             if (tag.length() > 30) {
//                 throw new IllegalArgumentException("해시태그는 30자 이하로 입력해야 합니다: " + tag);
//             }
    
//             tags[i] = tag;
//         }

//         // 콤마로 연결된 문자열로 반환
//         return String.join(",", tags);
//     }

//     // 해시태그로 게시글 검색
//     public List<BoardDTO> searchByHashTag(String tag) {
//         if (tag == null || tag.isEmpty()) {
//             throw new IllegalArgumentException("해시태그는 비어 있을 수 없습니다.");
//         }

//         // # 제거 (클라이언트에서 입력받은 값 처리)
//         String processedTag = tag.replaceFirst("#", "");

//         // 레포지토리에서 검색
//         List<Board> boards = boardRepository.findByHashTag(processedTag);

//         // 결과를 DTO로 변환
//         return boards.stream()
//                 .map(this::convertToDTO)
//                 .collect(Collectors.toList());
//     }


//     // 엔티티를 DTO로 변환
//     public BoardDTO convertToDTO(Board board) {
//         String hashTags = board.getHashTag();
//         String processedHashTags = null;
    
//         if (hashTags != null && !hashTags.isEmpty()) {
//             // 이미 #이 붙어 있으므로 중복으로 #을 붙이지 않도록 수정
//             processedHashTags = String.join(" ",
//                     Stream.of(hashTags.split(","))
//                             .map(String::trim) // 불필요한 공백 제거
//                             .collect(Collectors.toList())
//             );
//         }

//         // 이미지 서비스에서 이미지 목록을 가져옴
//         List<ImageDTO> images = imageService.getImagesByBoardId(board.getBoardId());
    
//         return new BoardDTO(
//                 board.getBoardId(),
//                 board.getUserId(),
//                 board.getContent(),
//                 board.getCDate(),
//                 processedHashTags, // null 또는 해시태그 문자열 반환
//                 board.getHidden(),
//                 board.getTitle(),
//                 board.getView(),
//                 board.getType(),
//                 board.getStatus(),
//                 board.getLastSavedAt(),
//                 images
//         );
//     }

//     // DTO를 엔티티로 변환
//     private Board convertToEntity(BoardDTO boardDTO) {
//         return new Board(
//                 boardDTO.getBoardId(),
//                 boardDTO.getUserId(),
//                 boardDTO.getContent(),
//                 boardDTO.getcDate(),
//                 boardDTO.getHashTag(),
//                 boardDTO.getHidden(),
//                 boardDTO.getTitle(),
//                 boardDTO.getView(),
//                 boardDTO.getType(),
//                 boardDTO.getStatus(),
//                 boardDTO.getLastSavedAt()
//         );
//     }
// }
