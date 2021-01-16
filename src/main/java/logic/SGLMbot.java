package logic;

import logic.DTO.UserDTO;
import logic.items.GlataiBeer;
import logic.items.LakaiWhisky;
import logic.items.MazaiScrub;
import logic.items.SasaiHoney;
import logic.utils.CurrencyParser;
import logic.utils.HibernateUtil;
import logic.utils.UserMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.persistence.Query;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SGLMbot extends TelegramLongPollingBot {

    private String username;
    private String token;
    private static SGLMbot sglmBot;
    private static final Core core = Core.getInstance();
    public static final Logger MSG_LOG = LogManager.getLogger("msg");
    public static final Logger INFO = LogManager.getLogger("csl");

    private SGLMbot() {
        setProperties();
        loadUsers();
        updateUsersData();
    }

    public static SGLMbot getInstance() {
        if (sglmBot == null) {
            sglmBot = new SGLMbot();
            System.out.println("==== bot instance created ====");
        }
        return sglmBot;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasMessage()) {
            checkUserInDB(update);
            if(update.getMessage().hasText()) {
                textMessageHandler(update);
            }

            //photo message
        /*if (update.hasMessage() && update.getMessage().hasPhoto()) {
            SendPhoto message = photoMessageHandler(update);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
                MSG_LOG.error("Was sent by bot: " + message.getCaption() + " on message: "
                        + update.getMessage().getText() + " from chatId: " + update.getMessage().getChatId());
            }
        }*/

            if(update.getMessage().hasContact()) {
                update.getMessage().getContact().getPhoneNumber();
                addUserPhoneNumber(update);
            }
        }


    }

    private void setProperties() {
        try (InputStream in = new FileInputStream("src/main/resources/config.properties")) {
            Properties prop = new Properties();
            prop.load(in);
            username = prop.getProperty("username");
            token = prop.getProperty("token");
        } catch (IOException e) {
            System.out.println("... an error occurred while loading properties");
        }
        System.out.println("... bot properties set successfully");
    }

    public static boolean isCommand(String text) {
        return text.startsWith("/");
    }

    private static ReplyKeyboardMarkup getReplyKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow firstRow = new KeyboardRow();
        KeyboardRow secondRow = new KeyboardRow();
        KeyboardRow thirdRow = new KeyboardRow();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

/*        keyboard.clear();
        firstRow.clear();
        secondRow.clear();*/

        firstRow.add(Commands.ABOUT_BUTTON);
        firstRow.add(Commands.SASAI_BUTTON);
        firstRow.add(Commands.GLATAI_BUTTON);
        secondRow.add(Commands.LAKAI_BUTTON);
        secondRow.add(Commands.MAZAI_BUTTON);

        KeyboardButton location = new KeyboardButton();
        location.setRequestLocation(true);
        location.setText("Вызвать курьера");
        KeyboardButton contact = new KeyboardButton();
        contact.setText("Зарегистрироваться");
        contact.setRequestContact(true);
        thirdRow.add(location);
        thirdRow.add(contact);

        keyboard.add(firstRow);
        keyboard.add(secondRow);
        keyboard.add(thirdRow);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

    private InlineKeyboardMarkup getButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton aboutButton = new InlineKeyboardButton();
        //aboutButton.setText("Обо мне");
        List<InlineKeyboardButton> rowOne = new ArrayList<>();
        rowOne.add(aboutButton);
        List<List<InlineKeyboardButton>> buttonsRow = new ArrayList<>();
        buttonsRow.add(rowOne);

        inlineKeyboardMarkup.setKeyboard(buttonsRow);

        return inlineKeyboardMarkup;
    }

    public static void saveUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            UserDTO userDTO = UserMapper.INSTANCE.userToUserDTO(user);
            session.save(userDTO);
            transaction.commit();
            INFO.info("userID: " + userDTO.getTelegramId() + " saved to DB.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateUserPhoneNumber(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("update UserDTO set phone_number = :number where telegram_id = " +
                    ":id");
            query.setParameter("number", user.getPhoneNumber());
            query.setParameter("id", user.getTelegramId());
            query.executeUpdate();

            transaction.commit();
            INFO.info("userID: " + user.getTelegramId() + " updated in DB.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateUserName(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("update UserDTO set user_name = :name where telegram_id = " +
                    ":id");
            query.setParameter("name", user.getName());
            query.setParameter("id", user.getTelegramId());
            query.executeUpdate();

            transaction.commit();
            INFO.info("userID UN: " + user.getTelegramId() + " updated in DB.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateUserFirstName(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("update UserDTO set first_name = :fname where telegram_id = " +
                    ":id");
            query.setParameter("fname", user.getFirstName());
            query.setParameter("id", user.getTelegramId());
            query.executeUpdate();

            transaction.commit();
            INFO.info("userID FN: " + user.getTelegramId() + " updated in DB.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateUserLastName(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("update UserDTO set last_name = :lname where telegram_id = " +
                    ":id");
            query.setParameter("lname", user.getLastName());
            query.setParameter("id", user.getTelegramId());
            query.executeUpdate();

            transaction.commit();
            INFO.info("userID LN: " + user.getTelegramId() + " updated in DB.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            List<UserDTO> userDTOList = session.createQuery("from UserDTO u", UserDTO.class).list();
            if(!userDTOList.isEmpty()) {
                for (UserDTO userDTO : userDTOList) {
                    User user = UserMapper.INSTANCE.userDTOToUser(userDTO);
                    core.addUser(user.getTelegramId(), user);
                }
                transaction.commit();
            }
            INFO.info("Total users found (loaded): ".concat(String.valueOf(userDTOList.size())).
                    concat(" (").concat(String.valueOf(core.getUserList().size())).concat(")"));
            System.out.println("... users were successfully loaded from DB");
        } catch (Exception e) {
            INFO.info("Total users found / (loaded): " + " / " + core.getUserList().size());
            System.out.println("... error while loading from DB");
            e.printStackTrace();
        }
    }

    private static void updateUsersData() {

   /*     for(Map.Entry<Integer, User> entry: core.getUserList().entrySet()) {
            entry.getValue();
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();


            transaction.commit();
            INFO.info("userID: " + " updated in DB.");
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }

// не принимает сообщения от некоторых пользователей (из-за NULL в фамилии валидатор не работает)
    private static void checkUserInDB(Update update) {
        int id = update.getMessage().getFrom().getId();
        if(!core.getUserList().containsKey(id)) {
            User user = new User(id, update.getMessage().getChatId(), update.getMessage().getFrom().getUserName(),
                    update.getMessage().getFrom().getFirstName(), update.getMessage().getFrom().getLastName());
            user.setOrdinalNumber(core.getUserList().size() + 1);
            core.addUser(id, user);
            saveUser(user);
        } else {
            User user = core.getUserList().get(id);
            //org.telegram.telegrambots.meta.api.objects.User = update.getMessage().getFrom();
            if(user.getName() != null && !user.getName().equals(update.getMessage().getFrom().getUserName())) {
                user.setName(update.getMessage().getFrom().getUserName());
                updateUserName(user);
            }
            if(user.getFirstName() != null && !user.getFirstName().equals(update.getMessage().getFrom().getFirstName())) {
                user.setFirstName(update.getMessage().getFrom().getFirstName());
                updateUserFirstName(user);
            }
            if(user.getLastName() != null && !user.getLastName().equals(update.getMessage().getFrom().getLastName())) {
                user.setLastName(update.getMessage().getFrom().getLastName());
                updateUserLastName(user);
            }

        }
    }

    private static void addUserPhoneNumber(Update update) {
        int id = update.getMessage().getFrom().getId();
        User user = core.getUserList().get(id);
        user.setPhoneNumber(update.getMessage().getContact().getPhoneNumber());
        System.out.println(user.getPhoneNumber());
        updateUserPhoneNumber(user);
    }

    private void textMessageHandler(Update update) {
        String text = update.getMessage().getText();
        MSG_LOG.info("[Message]: " + text + " [UserName]: " + update.getMessage().getFrom().getUserName()
                + " [UserId]: " + update.getMessage().getFrom().getId() + " [fname/lname]: "
                + update.getMessage().getFrom().getFirstName() + " " +
                update.getMessage().getFrom().getLastName());

        if (isCommand(text)) {
            if(text.equalsIgnoreCase("/start")) {
                sendTextMessage(update, Commands.HELLO, true);
            } else if(text.equalsIgnoreCase("/btc")) {
                sendTextMessage(update, CurrencyParser.getBTCPrice(), false);
            } else if(text.equalsIgnoreCase("/eth")) {
                sendTextMessage(update, CurrencyParser.getETHPrice(), false);
            } else if(text.equalsIgnoreCase("/usd")) {
                sendTextMessage(update, CurrencyParser.getUSDprice(), false);
            } else if(text.equalsIgnoreCase("/eur")) {
                sendTextMessage(update, CurrencyParser.getEURprice(), false);
            } else if(text.equalsIgnoreCase("/name")) {
                String messageText = "Приветствую вас, ".concat(update.getMessage().getFrom().getFirstName())
                        .concat("!");
                sendTextMessage(update, messageText, false);
            } else if(text.equalsIgnoreCase("/num")) {
                sendTextMessage(update, String.valueOf(core.getUserCount()), false);
            } else if(text.equalsIgnoreCase("/lst")) {
                String userList = "";
/*                core.getUserList().forEach((k, v) -> {
                    userList = userList.concat(v.getFirstName().concat("\n"));
                });*/
                for (Map.Entry<Integer, User> e : core.getUserList().entrySet()) {
                    userList = userList.concat("- ").concat(e.getValue().getFirstName()).concat(": ")
                            .concat(e.getValue().getChatId().toString()).concat("\n");
                }
                sendTextMessage(update, userList, false);
            } else if(text.matches("/msg \\d+ .+")) {
                SendMessage sendMessage = new SendMessage();
                Matcher matcher = Pattern.compile("/msg (?<id>\\d+) (?<message>.+)").matcher(text);
                matcher.find();
                String chatId = matcher.group("id");
                String message = matcher.group("message");
                sendMessage.setChatId(chatId);
                sendMessage.setText(message);
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                    MSG_LOG.error("Was sent by bot: " + sendMessage.getText() + " on message: "
                            + update.getMessage().getText() + " from chatId: " + update.getMessage().getChatId());
                }
            } else {
                sendTextMessage(update, "Неизвестная команда", true);
            }
        } else if(text.equals(Commands.ABOUT_BUTTON)) {
            sendTextMessage(update, Commands.ABOUT, true);
        } else if(text.equals(Commands.SASAI_BUTTON)) {
            sendPhotoMessage(update, SasaiHoney.getFileId(), SasaiHoney.getCaption(), true);
        } else if(text.equals(Commands.GLATAI_BUTTON)) {
            sendPhotoMessage(update, GlataiBeer.getFileId(), GlataiBeer.getCaption(), true);
        } else if(text.equals(Commands.LAKAI_BUTTON)) {
            sendTextMessage(update, LakaiWhisky.getCaption(), true);
        } else if(text.equals(Commands.MAZAI_BUTTON)) {
            sendTextMessage(update, MazaiScrub.getCaption(), true);
        } else if(text.equals("Геолокация")) {
            //message.setText("GOTTCHA 5");
        } else {
            sendTextMessage(update, "Я еще очень глуп и не понимаю, воспользуйтесь командами."
                    , true);
        }
    }

    private void sendTextMessage(Update update, String message, boolean keyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        sendMessage.setText(message);
        if(keyboardMarkup) {
            sendMessage.setReplyMarkup(getReplyKeyboard());
        }
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            MSG_LOG.error("Was sent by bot: " + sendMessage.getText() + " on message: "
                    + update.getMessage().getText() + " from chatId: " + update.getMessage().getChatId());
        }
    }

    private void sendPhotoMessage(Update update, String fileId, String caption, boolean keyboardMarkup) {
        SendPhoto photoMessage = new SendPhoto();
        photoMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        photoMessage.setPhoto(new InputFile(fileId));
        photoMessage.setCaption(caption);
        if(keyboardMarkup) {
            photoMessage.setReplyMarkup(getReplyKeyboard());
        }
        try {
            execute(photoMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            MSG_LOG.error("Was sent by bot: " + photoMessage.getPhoto().toString() + " on message: "
                    + update.getMessage().getText() + " from chatId: " + update.getMessage().getChatId());
        }
    }

    private SendPhoto photoMessageHandler(Update update) {
        SendPhoto message = new SendPhoto();
        message.setChatId(String.valueOf(update.getMessage().getChatId()));
        message.setPhoto(new InputFile("AgACAgIAAxkBAAIB8F_RM2tu9WW5o0IZLpjBbOHkGvCXAALAsjEbAkCISqn" +
                "9ISBLkvSZv52jli4AAwEAAwIAA20AA8QUBQABHgQ"));
        message.setCaption("САСАЙ МЁД - САМЫЙ ЛУЧШИЙ И ЭКОЛОГИЧЕСКИ ЧИСТЫЙ МЁД!");

        return message;
    }

    private SendMessage photoMessageIDFinder(Update update){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(update.getMessage().getChatId()));
        List<PhotoSize> photoSize = update.getMessage().getPhoto();
        message.setText(photoSize.get(0).getFileId() + " " + photoSize.get(0).getFileUniqueId() +
                " " + photoSize.get(0).getFilePath());
        MSG_LOG.info("photo list size: " + photoSize.size());
        return message;
    }

}
