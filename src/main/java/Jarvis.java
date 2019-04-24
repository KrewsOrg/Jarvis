import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.rooms.Room;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.habbohotel.users.HabboInfo;
import com.eu.habbo.habbohotel.users.HabboManager;
import com.eu.habbo.plugin.EventHandler;
import com.eu.habbo.plugin.EventListener;
import com.eu.habbo.plugin.HabboPlugin;
import com.eu.habbo.plugin.events.support.SupportTicketEvent;
import com.eu.habbo.plugin.events.support.SupportUserBannedEvent;
import com.eu.habbo.plugin.events.users.UserCommandEvent;
import com.eu.habbo.plugin.events.users.UserLoginEvent;
import com.mrpowergamerbr.temmiewebhook.DiscordEmbed;
import com.mrpowergamerbr.temmiewebhook.DiscordMessage;
import com.mrpowergamerbr.temmiewebhook.TemmieWebhook;
import com.mrpowergamerbr.temmiewebhook.embed.FieldEmbed;
import com.mrpowergamerbr.temmiewebhook.embed.FooterEmbed;
import com.mrpowergamerbr.temmiewebhook.embed.ThumbnailEmbed;

import java.util.Arrays;


public class Jarvis extends HabboPlugin implements EventListener {
    public static Jarvis INSTANCE = null;
    static TemmieWebhook banLog;
    static TemmieWebhook reportLog;
    static TemmieWebhook commandLog;




    @Override
    public void onEnable()
    {
        Emulator.getLogging().logStart("Hello Mr Stark, Jarvis here for all your habbo logging needs.");
        Emulator.getPluginManager().registerEvents(this, this);
    }


    @Override
    public void onDisable()
    {
        Emulator.getLogging().logShutdownLine("Sir, it appears as though something has gone wrong.");
    }


    @Override
    public boolean hasPermission(Habbo habbo, String s)
    {
        return false;
    }


    @EventHandler
    public static void onUserBannedEvent(SupportUserBannedEvent event) {
        String url = Emulator.getConfig().getValue("jarvis.banlog.discord.link");
        banLog = new TemmieWebhook(url);
        Habbo staff = Emulator.getGameEnvironment().getHabboManager().getHabbo(event.ban.staffId);
        HabboInfo target = HabboManager.getOfflineHabboInfo(event.ban.userId);

        DiscordEmbed de = DiscordEmbed.builder()
                .title(target.getUsername() + " has been banned!") // We are creating a embed with this title...
                .description("A User has been banned for breaking the rules!") // with this description...
                .url("https://google.com") // that, when clicked, goes to the TemmieWebhook repo...
                .footer(FooterEmbed.builder() // with a fancy footer...
                        .text("Jarvis is always watching.") // this footer will have the text "TemmieWebhook!"...
                        .icon_url("http://vignette2.wikia.nocookie.net/undertale-brasil/images/4/4f/Temmie.jpg/revision/latest?cb=20160221005012&path-prefix=pt-br") // with this icon on the footer
                        .build()) // and now we build the footer...
                .thumbnail(ThumbnailEmbed.builder() // with a fancy thumbnail...
                        .url("https://i.kym-cdn.com/entries/icons/original/000/000/615/BANHAMMER.png") // with this thumbnail...
                        .height(320) // not too big because we don't want to flood the user chats with a huge image, right?
                        .build()) // and now we build the thumbnail...
                .fields(Arrays.asList( // with fields...
                        FieldEmbed.builder()
                                .name("Ban Info")
                                .value("Banned User: " + target.getUsername() + "\n" +
                                        "Ban Type: " + event.ban.type.getType() + "ban" + "\r" + "Banned by: " +
                                        (staff != null ? staff.getHabboInfo().getUsername() : " UNKNOWN") + "\r" + "Duration: " + ((event.ban.expireDate - Emulator.getIntUnixTimestamp()) / 86400) + " days" + "\r" + "Reason: " + event.ban.reason)
                                .build()
                ))
                .build(); // and finally, we build the embed

        DiscordMessage dm = DiscordMessage.builder()
                .username("Jarvis") // We are creating a message with the username "Temmie"...
                .content("") // with no content because we are going to use the embed...
                .avatarUrl("https://pbs.twimg.com/profile_images/1115703751472365568/4BLviK1m_400x400.jpg") // with this avatar...
                .embeds(Arrays.asList(de)) // with the our embed...
                .build(); // and now we build the message!

        banLog.sendMessage(dm);

    }
    @EventHandler
    public static void onModToolTicket(SupportTicketEvent event)
    {
        String url = Emulator.getConfig().getValue("jarvis.reportlog.discord.link");
        reportLog = new TemmieWebhook(url);
        Room room = Emulator.getGameEnvironment().getRoomManager().getRoom(event.ticket.roomId);
        String message = "@here";
// Username, Content, Avatar URL
        DiscordMessage dm2 = new DiscordMessage("Jarvis", message, "https://pbs.twimg.com/profile_images/1115703751472365568/4BLviK1m_400x400.jpg");
        reportLog.sendMessage(dm2);
        DiscordEmbed de = DiscordEmbed.builder()
                .title(event.ticket.reportedUsername + " has been done something naughty!") // We are creating a embed with this title...
                .description("A User has made a call for help!") // with this description...
                .url("https://nextgenhabbo.com/client/" + room.getId()) // that, when clicked, goes to the TemmieWebhook repo...
                .footer(FooterEmbed.builder() // with a fancy footer...
                        .text("Jarvis is always watching.") // this footer will have the text "TemmieWebhook!"...
                        .icon_url("http://vignette2.wikia.nocookie.net/undertale-brasil/images/4/4f/Temmie.jpg/revision/latest?cb=20160221005012&path-prefix=pt-br") // with this icon on the footer
                        .build()) // and now we build the footer...
                .thumbnail(ThumbnailEmbed.builder() // with a fancy thumbnail...
                        .url("https://openclipart.org/image/2400px/svg_to_png/290024/emojipoo-remix.png") // with this thumbnail...
                        .height(128) // not too big because we don't want to flood the user chats with a huge image, right?
                        .build()) // and now we build the thumbnail...
                .fields(Arrays.asList( // with fields...
                        FieldEmbed.builder()
                                .name("Report Information")
                                .value("Message: " + event.ticket.message)
                                .build(),
                        FieldEmbed.builder()
                                .name("User Information")
                                .value("User who's naughty: " + event.ticket.reportedUsername + "\n" + "Reported by: " + event.ticket.senderUsername)
                                .build(),
                        FieldEmbed.builder()
                                .name("Room Information")
                                .value("Room Name: " + room.getName() + "\n" + "Room Owner: " + room.getOwnerName() +  "\n" + "Room ID: " + room.getId())
                                .build(),
                        FieldEmbed.builder()
                                .name("Ticket Information")
                                .value("ID: " + event.ticket.id)
                                .build()
                ))
                .build(); // and finally, we build the embed

        DiscordMessage dm = DiscordMessage.builder()
                .username("Jarvis") // We are creating a message with the username "Temmie"...
                .content("") // with no content because we are going to use the embed...
                .avatarUrl("https://pbs.twimg.com/profile_images/1115703751472365568/4BLviK1m_400x400.jpg") // with this avatar...
                .embeds(Arrays.asList(de)) // with the our embed...
                .build(); // and now we build the message!

        reportLog.sendMessage(dm);
    }
    public static String[] toIgnore = new String[]{};

    @EventHandler
    public static void onUserCommands(UserCommandEvent event) {
        if (event.succes && event.habbo.getHabboInfo().getRank().isLogCommands()) {
            for (String key : toIgnore) {
                if (event.args[0].toLowerCase().equalsIgnoreCase(key)) {
                    return;
                }
            }

            String message = event.habbo.getHabboInfo().getUsername() + " used the command :" + "  :";
            for (String s : event.args) {
                message += s + " ";
            }

            String url = Emulator.getConfig().getValue("jarvis.commandlog.discord.link");
            Habbo habbo = event.habbo;
            commandLog = new TemmieWebhook(url);
            DiscordMessage dm = DiscordMessage.builder()
                    .username("Jarvis") // We are creating a message with the username "Temmie"...
                    .content(message) // with no content because we are going to use the embed...
                    .avatarUrl("https://pbs.twimg.com/profile_images/1115703751472365568/4BLviK1m_400x400.jpg")// with this avatar....build(); // and now we build the message!
                    .build();
            commandLog.sendMessage(dm);
        }
    }

    @EventHandler
    public static void onUserLoginEvent(UserLoginEvent event) {
            String url = Emulator.getConfig().getValue("jarvis.loginlog.discord.link");
            Habbo habbo = event.habbo;
            commandLog = new TemmieWebhook(url);
            DiscordMessage dm = DiscordMessage.builder()
                    .username("Jarvis")
                    .content(event.habbo.getHabboInfo().getUsername() + " (" + event.habbo.getHabboInfo().getIpLogin()+ ")" + " has just logged into the client!")
                    .avatarUrl("https://pbs.twimg.com/profile_images/1115703751472365568/4BLviK1m_400x400.jpg")
                    .build();
            commandLog.sendMessage(dm);
        }
    }


