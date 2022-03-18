#CHESS
Chess hay trong tiếng Việt còn lại là Cờ vua, đôi khi còn được gọi là cờ phương Tây hoặc cờ quốc tế để phân biệt với các biến thể như cờ tướng, là một trò chơi board game dành cho hai người.

##Chess Rule
Tham khảo thông tin tại link bên dưới:
https://en.wikipedia.org/wiki/Rules_of_chess

##Install to use
Các file để chạy thì đã được output tại folder **build_folder**:
- Folder **server**: bố trí tại PC sẽ làm host.
- Folder **app**: bố trí tại các PC của người chơi.
- Chương trình sẽ ghi log vào thư mục log để có thể điều tra khi phát sinh error. Vui lòng post issue nếu có phát sinh vấn đề.

##How to use?
Chương trình chia làm 2 phần chính:

###Server
- Thông tin IP là cố định theo máy và không thể thay đổi sau khi khởi động.
- Port của Server tại field Port.
- Click "Start" để khởi động Server.
- Chỉ hỗ trợ kết nối trong cùng mạng local


###App
- Server phải khởi động trước mới có thể sử dụng.
- Chỉnh file server.txt trong thư mục config tương ứng với thông số của server.  
- Khi mới vào cần input tên ít nhất là 3 ký tự và nhiều nhất là 10 ký tự.

####Create And Join
- Để tạo bàn chơi mới click vào "New Game" trên giao diện chính.
- Màn hình setting thông tin cho game sẽ được hiển thị
  - Chọn màu quân mong muốn (White Or Black)
  - Chọn thời gian cho mỗi người chơi
- Để Join vào bàn chơi có sẵn thì:
  - Tại giao diện chính, sẽ có hiển thị danh sách bàn chơi đang có
  - Click vào phía bên trái (White) hoặc bên phải (Black) của bàn chơi để chọn phe
  - Click vào phía dưới bàn chơi để vào xem
  >- Bàn chơi có màu tối nghĩa là trò chơi chưa bắt đầu, 1 trong 2 bên trái/phải mà không có hình của người chơi thể Join.
  >- Bàn chơi có màu sáng nghĩa là trò chơi đã bắt đầu, chỉ có thể vào xem. 

####Play
- Sau khi tạo bàn chơi thì sẽ hiện giao diện bàn chơi như bên dưới
- Nếu bạn là người tạo ra bàn chơi thì bạn có quyển **Kick** người chơi khác nếu không thích đối thủ.
- Nếu người tạo bàn thoát ra khỏi và bạn là người chơi còn lại bạn được cấp quyền là người tạo bàn để sử dụng quyền **Kick**
- Để bắt đầu chơi bấm nút **OK** để sẵn sàng và đợi người chơi khác. Nếu người chơi khác cũng sẵn sàng thì trò chơi sẽ bắt đầu.
- Khi bắt đầu chơi sẽ có thời gian đếm ngược, nếu để hết thời gian trước đối thủ thì sẽ bị thua
- Click **Undo** khi muốn đi lại nước đi trước đó, tuy nhiên cần thực hiện ít nhất 1 nước đi trước đó và phải được đối thủ đồng ý. Khi đi lại được thực hiện, muốn đi lại tiếp cần phải thực hiện ít nhất 1 nước đi tiếp.
- Click **Draw** khi muốn xin hòa, và phải được đối thủ đồng ý.
- Click **Lose** khi muốn nhận thua, và ván game sẽ kết thúc.
  > Điều kiện để **Draw** và **Lose** là tổng nước đi cả 2 bên phải đi ít nhất là 6 nước (có thể chiếu bí người khác trong vòng 4 nước đi).
- Nếu  click **Exit** đồng nghĩa với bạn nhận thua và thoát khỏi phòng.
- Người chơi có thể chat với nhau bằng cách sử dụng khung chat bên dưới bàn chơi.

