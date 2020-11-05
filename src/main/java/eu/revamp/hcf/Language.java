package eu.revamp.hcf;

import eu.revamp.hcf.file.ConfigFile;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.chat.color.Replacement;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Getter
public enum Language {
    //----PREFIX----//
    PREFIX("PREFIX", "&3&lRevampMC &8» "),
    //----COMMANDS----//
    COMMANDS_FOR_PLAYER_USE_ONLY("COMMANDS.FOR_PLAYER_USE_ONLY", "{prefix} &cNo console."),
    COMMANDS_FOR_CONSOLE_ONLY("COMMANDS.FOR_CONSOLE_USE_ONLY", "{prefix} &cFor console only."),
    COMMANDS_NO_PERMISSION_MESSAGE("COMMANDS.NO_PERMISSION_MESSAGE", "{prefix} &cNo permission."),
    COMMANDS_FORBIDDEN_COMMAND_MESSAGE("COMMANDS.FORBIDDEN_COMMAND_MESSAGE", "{prefix} &cThis command is &lForbidden&c!"),
    COMMANDS_INVALID_NUMBER("COMMANDS.INVALID_NUMBER", "{prefix} &cPlease use a valid Number!"),
    COMMANDS_PLAYER_NOT_ONLINE("COMMANDS.PLAYER_NOT_ONLINE", "{prefix} &cPlayer &l%player% &cis not online!"),
    COMMANDS_NO_ONE_ONLINE("COMMANDS.NO_ONE_ONLINE", "{prefix} &cThere is no player online!"),
    COMMANDS_CANNOT_BE_INTEGER("COMMANDS.CANNOT_BE_INTEGER", "{prefix} &cThis can't be an integer!"),
    COMMANDS_MUST_BE_INTEGER("COMMANDS.MUST_BE_INTEGER", "{prefix} &cThis must be an integer!"),
    COMMANDS_INVENTORY_FULL("COMMANDS.INVENTORY_FULL", "{prefix} &cYour inventory is full."),
    COMMANDS_MUST_BE_ITEM("COMMANDS.MUST_BE_ITEM", "{prefix} &cInvalid item."),
    COMMANDS_PLAYER_NOT_FOUND("COMMANDS.PLAYER_NOT_FOUND", "{prefix} &cPlayer not found."),
    COMMANDS_SOMETHING_WRONG("COMMANDS.SOMETHING_WRONG", "{prefix} &cYou did somethings wrong."),
    COMMANDS_INVALID_DURATION("COMMANDS.INVALID_DURATION", "{prefix} &cInvalid Duration."),
    COMMANDS_INVALID_MATERIAL("COMMANDS.INVALID_MATERIAL", "{prefix} &cInvalid Material."),
    COMMANDS_NO_KITMAP("COMMANDS.NO_KITMAP", "{prefix} &cYou can't use this command on KitMap."),
    //----BALANCE----//
    BALANCE_OWN("BALANCE.OWN", "&6Balance: &f$%balance%&e."),
    BALANCE_OTHER("BALANCE.OTHER", "%player%'s &6Balance: &f$%balance%&e."),
    BALANCE_ADDED("BALANCE.ADDED", "&eYou have successfully added &f$%money% &eto &f%player%'s &eaccount."),
    BALANCE_ADDED_OTHER("BALANCE.ADDED_OTHER", "&f%player% &ehas added &f$%money% &eto your account."),
    BALANCE_SET("BALANCE.SET", "&eYou have successfully set balance of &f%player% &eto &f$%money%&e."),
    BALANCE_SET_OTHER("BALANCE.SET_OTHER", "&f%player% &ehas set your balance to &f$%money% &e."),
    BALANCE_GIVEALL("BALANCE.GIVEALL", "&7(&4&l*&7) &6&l%player% &7has gave all online players &6&l$%money%&7."),
    //----BACK----//
    BACK_TELEPORTED("BACK_TELEPORTED", "&fTeleported &eto your last location!"),
    BACK_NO_LOCATION("BACK.NO_LOCATION", "&eCan''t find last &flocation&e!"),
    //----DELWARP----//
    DELWARP_COMMAND_USAGE("DELWARP.COMMAND_USAGE", "&fUsage&e: /&fdelwarp &e(&fname&e)"),
    DELWARP_DELETED("DELWARP.DELETED", "&eWarp &f%warp% &ehas been &fsuccessfully &edeleted!"),
    DELWARP_NOT_FOUND("DELWARP.NOT_FOUND", "&eWarp not found."),
    //----EXP----//
    EXP_COMMAND_USAGE("EXP.COMMAND_USAGE", "&fUsage&e: /&fexp set &e(&fplayer&e) &e(&famount&e)"),
    EXP_SET("EXP.SET", "&eYou have set your &fexp &eto &f%exp%&e!"),
    EXP_SET_OTHER("EXP.SET_OTHER", "&eYou have set &f%player%&'s exp &to &f%exp%&e!"),
    EXP_SET_OTHER_TARGET("EXP.SET_OTHER_TARGET", "&eYour exp was set to &f%exp% &eby &f%player%&e!"),
    //----GOD (lol)----//
    GOD_ENABLED("GOD.ENABLED", "&eYou have &aenabled &egod mode!"),
    GOD_DISABLED("GOD.DISABLED", "&eYou have &cdisabled &egod mode!"),
    GOD_ENABLED_OTHER("GOD.ENABLED_OTHER", "&eYou have &aenabled &f%player%'s &egod mode!"),
    GOD_DISABLED_OTHER("GOD.DISABLED_OTHER", "&eYou have &cdisabled &f%player%'s &egod mode!"),
    GOD_ENABLED_OTHER_TARGET("GOD.ENABLED_OTHER_TARGET", "&eYour god mode was &aenabled &eby &f%player%&e!"),
    GOD_DISABLED_OTHER_TARGET("GOD.DISABLED_OTHER_TARGET", "&eYour god mode was &cdisabled &eby &f%player%&e!"),
    //----HCESSENTIALS----//
    HCESSENTIALS_RELOAD("HCESSENTIALS.RELOAD", "&eYou have &fsuccessfully &ereloaded all configs!"),
    //----ITEM----//
    ITEM_COMMAND_USAGE("ITEM.COMMAND_USAGE", "&fUsage&e: /&fitem &e(&fitem&e) &e(&famount&e)"),
    ITEM_GIVEN("ITEM.GIVEN", "&f<amount> &eof &f<item> &ehave been added to your inventory!"),
    //----KICKALL----//
    KICKALL_COMMAND_USAGE("KICKALL.COMMAND_USAGE", "&fUsage&e: /&fkickall &e%&fmessage&e%"),
    KICKALL_KICKED("KICKALL.KICKED", "&f%player% &ehas kicked everyone for &f%reason%&e!"),
    //----KILLALL----//
    KILLALL_COMMAND_USAGE("KILLALL.COMMAND_USAGE", "&fUsage&e: /&fkillall &e<&fmobs|animals|items|all&e>"),
    KILLALL_KILLED_ALL("KILLALL.KILLED_ALL", "&eYou have &fsuccessfully &ekilled all entities!"),
    KILLALL_KILLED_MOBS("KILLALL.KILLED_MOBS", ""),
    KILLALL_KILLED_ANIMALS("KILLALL.KILLED_ANIMALS", "&eYou have &fsuccessfully &ekilled all animals!"),
    KILLALL_KILLED_ITEMS("KILLALL.KILLED_ITEMS", "&eYou have &fsuccessfully &ekilled all items!"),
    //----KILL----//
    KILL_COMMAND_USAGE("KILL.COMMAND_USAGE", "&fUsage&e: /&fkill &e(&fplayer&e)"),
    KILL_KILLED_OTHER("KILL.KILLED_OTHER", "&eYou have &fkilled &f%player%&e!"),
    KILL_KILLED_OTHER_TARGET("KILL.KILLED_OTHER_TARGET", "&eYou have been &fkilled &eby &f%player%&e!"),
    //----SEEN----//
    SEEN_COMMAND_USAGE("SEEN.COMMAND_USAGE", "&fUsage&e: /&fseen &e(&fplayer&e)"),
    SEEN_ONLINE("SEEN.ONLINE", "&ePlayer &f<player> &ehas been &aonline &esince &c<time>&e!"),
    SEEN_OFFLINE("SEEN.OFFLINE", "&ePlayer &f<player> &ehas been &coffline &esince &c<time>&e!'"),
    //----SPAWN----//
    SPAWN_SET("SPAWN.SET", "&eYou have &fsuccessfully &eset up &fspawn&e!"),
    SPAWN_TELEPORTED("SPAWN.TELEPORTED", "&eYou have been &fteleported &eto &fspawn&e!"),
    SPAWN_TELEPORTED_OTHER("SPAWN.TELEPORTED_OTHER", "&eYou have &fteleported <player> &eto &fspawn&e!"),
    SPAWN_TELEPORTED_OTHER_TARGET("SPAWN.TELEPORTED_OTHER_TARGET", "&eYou have been &fteleported &eto &fspawn &eby &f<player>&e!"),
    //----END----//
    END_USAGE("END.USAGE", "&cSetEnd - Help Commands {0} &c/setend spawn - Set end spawn point. {0} &c/setend exit - Set end exit point."),
    END_EXIT_SET("END.EXIT_SET", "&eYou have &fsuccessfully &eset &fthe &eend exit&e!"),
    END_SPAWN_SET("END.SPAWN_SET", "&eYou have &fsuccessfully &eset &fthe &eend spawn&e!"),
    //----SETWARP----//
    SETWARP_COMMAND_USAGE("SETWARP.COMMAND_USAGE", "&fUsage&e: /&fsetwarp &e(&fname&e)"),
    SETWARP_SET("SETWARP.SET", "&eWarp &f%warp% &ehas been &fsuccessfully &eset!"),
    SETWARP_WARP_EXISTS("SETWARP.WARP_EXISTS", "&eWarp &f%warp% &ealready exists!"),
    //----SOCIALSPY----//
    SOCIALSPY_ENABLED("SOCIALSPY.ENABLED", "&fSocialSpy &ehas been &aenabled&e!"),
    SOCIALSPY_DISABLED("SOCIALSPY.DISABLED", "&fSocialSpy &ehas been &cdisabled&e!"),
    SOCIALSPY_MESSAGE("SOCIALSPY.MESSAGE", "&e[&6SS&e] &6» &e(<player> &e-> <target>&e) &6» &f<message>"),
    //----SPAWNER----//
    SPAWNER_COMMAND_USAGE("SPAWNER.COMMAND_USAGE", "&fUsage&e: /&fspawner &e(&ftype&e)"),
    SPAWNER_CHANGED("SPAWNER.CHANGED", "&eSpawner &fsuccessfully changed &eto &f%type%&e!"),
    SPAWNER_DOES_NOT_EXIST("SPAWNER.DOES_NOT_EXIST", "&eMob &f%spawner% &edoesn''t exist!"),
    SPAWNER_MUST_BE_LOOKING_AT_SPAWNER("SPAWNER.MUST_BE_LOOKING_AT_SPAWNER", "&eYou &fmust &ebe looking at a &fspawner&e!"),
    //----TELEPORTALL----//
    TELEPORTALL_TELEPORTED("TELEPORTALL.TELEPORTED", "&f%player% &ehas &fteleported &eeveryone to his location!"),
    //----WARP----//
    WARP_DOES_NOT_EXIST("WARP.DOES_NOT_EXIST", "&eWarp &f%warp% &edoesn''t exist!"),
    WARP_NO_WARPS("WARP.NO_WARPS", "&eNo &fwarps &eare set up yet&e!"),
    WARP_TELEPORTED("WARP.TELEPORTED", "&eWarped to &f%warp%&e!"),
    WARP_LIST("WARP.LIST", "&fWarps&e: "),
    WARP_COLOR("WARP.COLOR", "&f"),
    WARP_COLOR_COMMA("WARP.COLOR_COMMA", "&e,"),
    //----PORTALFREEZE----//
    PORTALFREEZE_COMMAND_USAGE("PORTALFREEZE.COMMAND_USAGE", "&cCorrect Usage: /portalfreeze (playerName)."),
    PORTALFREEZE_LOCATION_IS_NULL("PORTALFREEZE.LOCATION_IS_NULL", "&cPortal location is null please contact Administrator."),
    PORTALFREEZE_FROZEN("PORTALFREEZE.FROZEN", ""),
    PORTALFREEZE_UNFROZEN("PORTALFREEZE.UNFROZEN", "&f%player% &ehas been portalfrozen by &f%sender%&e."),
    //----REBOOT----//
    REBOOT_COMMAND_USAGE("REBOOT.COMMAND_USAGE", "&4&lReboot - Help Commands {0} &c/Reboot start (time) - Start Reboot. {0} &c/Reboot end - Stop Reboot."),
    REBOOT_CANCELLED("REBOOT.CANCELLED", "&cYou have successfully cancelled &4&lReboot&c."),
    REBOOT_RUNNING("REBOOT.RUNNING", "&4&lReboot &ais already enabled, use /reboot cancel to end it."),
    REBOOT_TOO_SHORT("REBOOT.TOO_SHORT", "&4&lReboot &atime must last for at least 20 ticks."),
    REBOOT_STARTED("REBOOT.STARTED", "&cStarted &4&lReboot&a for &l%time%&c."),
    REBOOT_NOT_ACTIVE("REBOOT.NOT_ACTIVE", "&4&lReboot &cis not active."),
    //----SAVEDATA----//
    SAVEDATA_SAVING("SAVEDATA.SAVING", "&a&lSaving..."),
    SAVEDATA_SAVED("SAVEDATA.SAVED", "&aSuccessfully saved all factions to database."),
    //----SETSLOTS----//
    SETSLOTS_COMMAND_USAGE("SETSLOTS.COMMAND_USAGE", "&cUsage: /setslots (slots)."),
    SETSLOTS_SET("SETSLOTS.SET", "&eSet the maximum players to &a%slots%&e."),
    //----SET----//
    SET_COMMAND_USAGE("SET.COMMAND_USAGE", "&cSet - Help Commands {0} &c/set spawn - Set world spawn. {0} &c/set netherspawn - Set nether spawn. {0} &c/set endspawn - Set end spawn. {0} &c/set endexit - Set end exit. {0} &c/set portalfreeze - Set portalfreeze spawn point. {0} &c/set eotwffa - Set eotwffa spawn point."),
    SET_END_EXIT("SET.END_EXIT", "&aYou have successfully set End Exit."),
    SET_END_SPAWN("SET.END_SPAWN", "&aYou have successfully set End Spawn."),
    SET_SPAWN("SET.SPAWN", "&aYou have successfully set World Spawn."),
    SET_NETHER_SPAWN("SET.NETHER_SPAWN", "&aYou have successfully set Nether Spawn."),
    SET_PORTAL_FREEZE("SET.PORTAL_FREEZE", "&aYou have successfully set PortalFreeze spawn point."),
    SET_EOTW_FFA("SET.EOTW_FFA", "&aYou have successfully set EOTW-FFA spawn point."),
    //----FACTIONS----//
    FACTIONS_NOFACTION("FACTIONS.NOFACTION", "&cYou are not in a faction"),
    FACTIONS_FACTION_NOT_FOUND("FACTIONS.FACTION_NOT_FOUND", "&cFaction not found."),
    FACTIONS_ALREADY_IN_A_FACTION("FACTIONS.ALREADY_IN_A_FACTION", "&cYou are already in a faction."),
    //----UNALLY----//
    UNALLY_COMMAND_USAGE("UNALLY.COMMAND_USAGE", "&cCorrect Usage: /f unally (all|factionName)."),
    UNALLY_NO_ALLIES("UNALLY.NO_ALLIES", "&cYour faction don't have any allies."),
    UNALLY_ERROR("UNALLY.ERROR", "&eYour faction is not %relation% %faction%&e."),
    UNALLY_EVENT_CANCELLED("UNALLY.EVENT_CANCELLED", "&cCould not drop &l%relation% &cwith &l%faction%"),
    UNALLY_DROPPED("UNALLY.DROPPED", "&eYour faction has dropped its %relation% &ewith %faction%&e"),
    UNALLY_DROPPED_OTHER("UNALLY.DROPPED_OTHER", "%faction% &ehas dropped their %relation% &ewith your faction."),
    //----LEAVE----//
    LEAVE_LEFT("LEAVE.LEFT", "&aYou have successfully left the faction."),
    LEAVE_LEFT_OTHER("LEAVE.LEFT_OTHER", "%player% &ehas left the faction."),
    LEAVE_CAPTAIN("LEAVE.CAPTAIN", "&cYou can't just leave faction if you want to leave faction as leader please type /f disband or just give someone else leader."),
    //----F_LIST----//
    F_LIST_INVALID_PAGE("F_LIST.INVALID_PAGE", "&cYou cannot view a page less than 1."),
    //----KICK----//
    F_KICK_COMMAND_USAGE("F_KICK.COMMAND_USAGE", "&cCorrect Usage: /f kick (playerName)."),
    F_KICK_CANNOT_JOIN_RAIDABLE("F_KICK.CANNOT_JOIN_RAIDABLE", "&cYou cannot kick players while your faction is raidable."),
    F_KICK_KICKED("F_KICK.KICKED", "&cYou were kicked from the faction by %player%."),
    F_KICK_KICKED_OTHER("F_KICK.KICKED_OTHER", "&f%player% &ehas been kicked by &2%kicker%&e."),
    //----EOTW----//
    EOTW_FACTION_CREATE("EOTW.FACTION_CREATE", "&cYou can't create factions while &lEOTW &cis active."),
    EOTW_FACTION_CLAIM_CHANGE("EOTW.FACTION_CLAIM_CHANGE", "&cYou can't claim lands while &lEOTW &cis active."),
    //----DTR----//
    DTR_FULL("DTR.FULL", "&cYour faction currently has full DTR."),
    DTR_PAUSED("DTR.PAUSED", "&cYour faction is currently on DTR freeze for another &l%time%&c."),
    DTR_REGENERATING("DTR.REGENERATING", "&cYour faction currently has &l%dtr%&c DTR and is regenerating at a rate of &l%dtr2%&c every &l%time%&c. Your ETA for maximum DTR is &l%maxdtr%&c."),
    DTR_ERROR("DTR.ERROR", "&cError while checking your regen status please contact an Administrator."),
    //----POTIONEFFECT----//
    POTIONEFFECT_DISABLED("POTIONEFFECT.DISABLED", "&cThis Potion Effect is disabled."),
    //----PORTAL----//
    PORTAL_ENTER_TAGGED("PORTAL.ENTER_TAGGED", "&cYou cannot enter the End whilst your Spawn Tag timer is active"),
    PORTAL_ENTER_PVPTIMER("PORTAL.ENTER_PVPTIMER", "&cYou cannot enter the End whilst your PvP Protection timer is active"),
    //----FOCUS----//
    FOCUS_COMMAND_USAGE("FOCUS.COMMAND_USAGE", "&cCorrect Usage: /focus (playerName)."),
    FOCUS_FACTION_MEMBERS("FOCUS.FACTION_MEMBERS", "&cYou can't focus faction members."),
    FOCUS_ON("FOCUS.ON", " {0} &bYour faction is now focusing on &d%player%. {0} "),
    FOCUS_OFF("FOCUS.OFF", " {0} &cYour faction is no longer focusing &d%player%. {0} "),
    //----PAY----//
    PAY_COMMAND_USAGE("PAY.COMMAND_USAGE", "&cUsage: /pay (playerName) (amount)"),
    PAY_INVALID_MONEY("PAY.INVALID_MONEY", "&cInvalid money."),
    PAY_NOT_ENOUGH_MONEY("PAY.NOT_ENOUGH_MONEY", "&cYou only have &l%money%$ &con your account."),
    PAY_PAY_YOURSELF("PAY.PAY_YOURSELF", "&cYou cannot send money to your self."),
    PAY_MONEY_SENT("PAY.MONEY_SENT", "&eYou have successfully sent &f%player% &f$%money%&e."),
    PAY_MONEY_RECEIVED("PAY.MONEY_RECEIVED", "&f%player% &ehas sent you &f%money%$&e."),
    //----DEATHMESSAGES----//
    DEATHMESSAGES_ENABLED("DEATHMESSAGES.ENABLED", "&eDeathMessages receiving &aEnabled&e."),
    DEATHMESSAGES_DISABLED("DEATHMESSAGES.DISABLED", "&eDeathMessages receiving &cDisabled&e."),
    //----ARCHER----//
    ARCHER_MARK_ARMOR_GREEN_SHOOTER("ARCHER.MARK.ARMOR.GREEN.SHOOTER", "Since your armor is green, you gave %damaged% the poison effect for 6 seconds..."),
    ARCHER_MARK_ARMOR_GREEN_DAMAGED("ARCHER.MARK.ARMOR.GREEN.DAMAGED", "Since %shooter%'s armor is green, you were given the poison effect for 6 seconds..."),
    ARCHER_MARK_ARMOR_BLUE_SHOOTER("ARCHER.MARK.ARMOR.BLUE.SHOOTER", "Since your armor is blue, you gave %damaged% the slowness effect for 6 seconds..."),
    ARCHER_MARK_ARMOR_BLUE_DAMAGED("ARCHER.MARK.ARMOR.BLUE.DAMAGED", "Since %shooter%'s armor is blue, you were given the slowness effect for 6 seconds..."),
    ARCHER_MARK_ARMOR_GRAY_SHOOTER("ARCHER.MARK.ARMOR.GRAY.SHOOTER", "Since your armor is gray, you gave %damaged% the blindness effect for 6 seconds..."),
    ARCHER_MARK_ARMOR_GRAY_DAMAGED("ARCHER.MARK.ARMOR.GRAY.DAMAGED", "Since %shooter%'s armor is gray, you were given the blindness effect for 6 seconds..."),
    ARCHER_MARK_ARMOR_BLACK_SHOOTER("ARCHER.MARK.ARMOR.BLACK.SHOOTER", "Since your armor is black, you gave %damaged% the wither effect for 6 seconds..."),
    ARCHER_MARK_ARMOR_BLACK_DAMAGED("ARCHER.MARK.ARMOR.BLACK.DAMAGED", "Since %shooter%'s armor is black, you were given the wither effect for 6 seconds..."),
    //----ROGUE----//
    ROGUE_BACKSTABBED_ATTACKER("ROGUE.BACKSTABBED.ATTACKER", "&eYou have backstabbed &c%player%."),
    ROGUE_BACKSTABBED_DAMAGED("ROGUE.BACKSTABBED.DAMAGED", "&c%player% &ehas backstabbed you."),
    //----RANGER----//
    RANGER_NORMAL("RANGER.NORMAL", "&eYou're now in &aNormal &emode."),
    RANGER_DEMON_FORCED("RANGER.DEMON_FORCED", "&eYou've forced into &cDemon &emode &7&o(5 seconds)."),
    RANGER_DEMON_MODE("RANGER.DEMON_MODE", "&eYou're now in &cDemon &emode &7&o(5 seconds)."),
    RANGER_DEMON_MODE_OTHER("RANGER.DEMON_MODE_OTHER", "&eAn ranger has taken damage in stealth mode near you: &7&o(20 x 20)"),
    RANGER_STEALTH_MODE("RANGER.STEALTH_MODE", "&eYou're now in &7Stealth &emode"),
    //----MINER----//
    MINER_INVISIBILITY_ADDED("MINER.INVISIBILITY_ADDED", "%miner% &7invisibility added."),
    MINER_INVISIBILITY_REMOVED("MINER.INVISIBILITY_REMOVED", "%miner% &7invisibility removed."),
    //----BARD----//
    BARD_PVPTIMER_EQUIP("BARD.PVPTIMER_EQUIP", "&cYou can't equip %bard% Class while your&a&l PvP Protection&c is active."),
    BARD_ENERGY("BARD.ENERGY", "%bard% Energy: &a%energy%"),
    BARD_ENERGY_USED("BARD.ENERGY_USED", "&cYou have just used a &l%bard% Buff &cthat cost you %energycost% &cof your Energy."),
    BARD_NOT_ENOUGH_ENERGY("BARD.NOT_ENOUGH_ENERGY", "&cYou do not have enough energy for this! You need %energycost% energy, but you only have %energy%"),
    BARD_CANNOT_USE_SPAWN("BARD.CANNOT_USE_SPAWN", "%bard% effects cannot be used while in spawn."),
    BARD_CANNOT_USE_TOURNAMENT("BARD.CANNOT_USE_TOURNAMENT", "%bard% effects cannot be used while in tournament."),
    //----COMBAT----//
    COMBAT_TAGGED("COMBAT.TAGGED", "&eYou have been spawn-tagged for &c%time% &eseconds!"),
    COMBAT_PLACE("COMBAT.PLACE", "&cYou can't place blocks in combat"),
    COMBAT_BREAK("COMBAT.BREAK", "&cYou can't break blocks in combat"),
    COMBAT_JOIN_TAGGED("COMBAT.JOIN_TAGGED", "&cYou cannot join factions while your &c&lSpawn Tag&c timer is active."),
    COMBAT_LEAVE_TAGGED("COMBAT.LEAVE_TAGGED", "&cYou cannot leave factions whilst your &c&lSpawn Tag&c timer is active"),
    COMBAT_KICK_TAGGED("COMBAT.KICK_TAGGED", "&cYou cannot kick &l%player% &cas his &c&lSpawn Tag &ctimer is active."),
    //----STUCK----//
    STUCK_MOVED("STUCK.MOVED", "&cYou moved more than %distance% blocks, teleport cancelled!"),
    STUCK_DAMAGED("STUCK.DAMAGED", "&cYou took damage, teleportation cancelled!"),
    STUCK_TELEPORTED("STUCK.TELEPORTED", "&cYou have been teleported to the nearest safe area."),
    //----APPLE----//
    APPLE("APPLE", "&c\u2588\u2588\u2588\u2588\u2588&c\u2588\u2588\u2588 {0} &c\u2588\u2588\u2588&e\u2588\u2588&c\u2588\u2588\u2588 {0} &c\u2588\u2588\u2588&e\u2588&c\u2588\u2588\u2588\u2588 {0} &c\u2588\u2588&6\u2588\u2588\u2588\u2588&c\u2588\u2588 {0} &c\u2588&6\u2588\u2588&f\u2588&6\u2588&6\u2588\u2588&c\u2588 {0} &c\u2588&6\u2588&f\u2588&6\u2588&6\u2588&6\u2588\u2588&c\u2588 &6 Cooldown Remaining: %newline &c\u2588&6\u2588\u2588&6\u2588&6\u2588&6\u2588\u2588&c\u2588 &7  %time% {0} &c\u2588&6\u2588\u2588&6\u2588&6\u2588&6\u2588\u2588&c\u2588 {0} &c\u2588\u2588&6\u2588\u2588\u2588\u2588&c\u2588\u2588 {0} &c\u2588\u2588\u2588\u2588\u2588&c\u2588\u2588\u2588"),
    //----GAPPLE----//
    GAPPLE("GAPPLE", "&2\u2588\u2588\u2588\u2588\u2588&2\u2588\u2588\u2588 {0} &2\u2588\u2588\u2588&e\u2588\u2588&2\u2588\u2588\u2588 {0} &2\u2588\u2588\u2588&e\u2588&2\u2588\u2588\u2588\u2588 {0} &2\u2588\u2588&6\u2588\u2588\u2588\u2588&2\u2588\u2588 {0} &2\u2588&6\u2588\u2588&f\u2588&6\u2588&6\u2588\u2588&2\u2588 {0} &2\u2588&6\u2588&f\u2588&6\u2588&6\u2588&6\u2588\u2588&2\u2588 &6 Cooldown Remaining: {0} &2\u2588&6\u2588\u2588&6\u2588&6\u2588&6\u2588\u2588&2\u2588 &7  %time% {0} &2\u2588&6\u2588\u2588&6\u2588&6\u2588&6\u2588\u2588&2\u2588 {0} &2\u2588\u2588&6\u2588\u2588\u2588\u2588&2\u2588\u2588 {0} &2\u2588\u2588\u2588\u2588\u2588&2\u2588\u2588\u2588"),
    //----KOTH----//
    KOTH("KOTH", "&8&m--------------------------------- {0} &8\u2588&e\u2588\u2588\u2588\u2588\u2588\u2588\u2588&8\u2588 {0} &e\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588 {0} &e\u2588&6\u2588&e\u2588&6\u2588&e\u2588&6\u2588&e\u2588&6\u2588&e\u2588 {0} &e\u2588&6\u2588\u2588\u2588\u2588\u2588\u2588\u2588&e\u2588        &a%koth% {0} &e\u2588&6\u2588&b\u2588&6\u2588&b\u2588&6\u2588&b\u2588&6\u2588&e\u2588 &7has been started. &a(%time%) {0} &e\u2588&6\u2588\u2588\u2588\u2588\u2588\u2588\u2588&e\u2588 {0} &e\u2588\u2588\u2588&7\u2588\u2588\u2588&e\u2588\u2588\u2588 {0} &e\u2588\u2588\u2588\u2588&7\u2588&e\u2588\u2588\u2588\u2588 {0} &8\u2588&e\u2588\u2588\u2588&7\u2588&e\u2588\u2588\u2588&8\u2588 {0} &8&m---------------------------------"),
    //----CONQUEST----//
    CONQUEST("CONQUEST", "&8&m--------------------------------- {0} &4\u2588&e\u2588\u2588\u2588\u2588\u2588\u2588\u2588&4\u2588 {0} &6\u2588\u2588&e\u2588\u2588\u2588\u2588\u2588&6\u2588\u2588 {0} &6\u2588\u2588&e\u2588&6\u2588&e\u2588&6\u2588&e\u2588&6\u2588\u2588 {0} &e\u2588&6\u2588\u2588\u2588\u2588\u2588\u2588\u2588&e\u2588        &a%koth% {0} &e\u2588&6\u2588&c\u2588&6\u2588&c\u2588&6\u2588&c\u2588&6\u2588&e\u2588 &7has been started. &a(%time%) {0} &e\u2588&6\u2588\u2588\u2588\u2588\u2588\u2588\u2588&e\u2588 {0} &4\u2588&e\u2588\u2588\u2588\u2588\u2588\u2588\u2588&4\u2588 {0} &8&m---------------------------------"),
    //----FREEZE----//
    //TODO ADD IN REVAMPSYSTEM
    FREEZE("FREEZE", "&f\u2588\u2588\u2588\u2588&c\u2588&f\u2588\u2588\u2588\u2588 {0} &f\u2588\u2588\u2588&c\u2588&6\u2588&c\u2588&f\u2588\u2588\u2588 {0} &f\u2588\u2588&c\u2588&6\u2588&0\u2588&6\u2588&c\u2588&f\u2588\u2588 {0} &f\u2588\u2588&c\u2588&6\u2588&0\u2588&6\u2588&c\u2588&f\u2588\u2588 {0} &f\u2588&c\u2588&6\u2588\u2588&0\u2588&6\u2588\u2588&c\u2588&f\u2588 {0} &f\u2588&c\u2588&6\u2588\u2588\u2588\u2588\u2588&c\u2588&f\u2588 {0} &c\u2588&6\u2588\u2588\u2588&0\u2588&6\u2588\u2588\u2588&c\u2588 {0} &c\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588 {0} &4&l(&c&l!&4&l)&6You have been frozen! Do not log out or you will be banned! {0} &6You have 5 minutes to join ts.revampmc.eu!"),
    //----LIVES----//
    LIVES_COMMAND_USAGE("LIVES.COMMAND_USAGE", "&cLives - Help Commands {0} &c/lives check (playerName) - Check player's lives. {0} &c/lives send (playerName) (amount) - Send player's lives. {0} &c/lives revive (playerName) - Revive player's with lives."),
    LIVES_CHECK("LIVES.CHECK", "&eYou currently have &f%lives% &elives."),
    LIVES_CHECK_OTHER("LIVES.CHECK_OTHER", "&f%player% &ehas &f%lives% &elives."),
    //----REVIVE----//
    LIVES_REVIVE_USAGE("LIVES.REVIVE_USAGE", "&cCorrect Usage: /lives revive (playerName)."),
    //----SEND----//
    LIVES_SEND_YOURSELF("LIVES.SEND_YOURSELF", "&cYou cannot send lives to your self."),
    LIVES_SEND_USAGE("LIVES.SEND_USAGE", "&cCorrect Usage: /lives send (playerName) (amount)."),
    LIVES_INSUFFICIENT_LIVES("LIVES.INSUFFICIENT_LIVES", "&cYou don't have &l%lives% &clives."),
    LIVES_NO_LIVES("LIVES.NO_LIVES", "&cYou only have &l0 &clives."),
    LIVES_NOT_IN_DB("LIVES.NOT_IN_DB", "&c&l%player% &cis not in database."),
    //----DEPOSIT_LIVES----//
    DEPOSIT_LIVES_RAIDABLE("DEPOSIT_LIVES.RAIDABLE", "&cYou cannot deposit lives into your faction when raidable."),
    DEPOSIT_LIVES_INVALID_LIVES("DEPOSIT_LIVES.INVALID_LIVES", "&cInvalid lives."),
    DEPOSIT_LIVES_NOT_ENOUGH_LIVES("DEPOSIT_LIVES.NOT_ENOUGH_LIVES", "&cYour lives after this transaction would be less than 0."),
    DEPOSIT_LIVES_DEPOSITED("DEPOSIT_LIVES.DEPOSITED", "&aYou have deposited &l%lives% &alives into the faction."),
    DEPOSIT_LIVES_DEPOSITED_OTHER("DEPOSIT_LIVES.DEPOSITED_OTHER", "&2%player% &ehas deposited &a%lives% &elives into the faction."),
    //----VISUALMAP----//
    VISUALMAP_TYPE_NOT_FOUND("VISUALMAP.TYPE_NOT_FOUND", "{prefix} &cVisual type &l%type%&c not found."),
    VISUALMAP_HIDDEN("VISUALMAP.HIDDEN", "{prefix} &cClaim pillars are no longer shown."),
    //----STAFFREVIVE----//
    STAFFREVIVE_COMMAND_USAGE("STAFFREVIVE.COMMAND_USAGE", "&cUsage: /staffrevive (playerName)"),
    //----LFF----//
    LFF_MESSAGE("LFF.MESSAGE", "&7&m------------------------------------- {0} &6%player% &eis looking for faction! {0} &eYou can uses looking for faction! {0} &7&m-------------------------------------"),
    LFF_COOLDOWN("LFF.COOLDOWN", "&cPlease wait %time% to use again."),
    //----NEWVIDEO----//
    NEWVIDEO_MESSAGE("NEWVIDEO.MESSAGE", "&7&m------------------------------------- &newline% &6%player% &euploaded a new video! {0} &6Video Link: &7%link% {0} &7&m-------------------------------------"),
    NEWVIDEO_COOLDOWN("NEWVIDEO.COOLDOWN", "&cPlease wait %time% to use again."),
    //----RECORDING----//
    RECORDING_MESSAGE("RECORDING.MESSAGE", "&7&m------------------------------------- {0} &6%player% &eis now recording! {0} &6Channel: &7%link% {0} &7&m-------------------------------------"),
    RECORDING_COOLDOWN("RECORDING.COOLDOWN", "&cPlease wait %time% to use again."),
    //----KEYSALE----//
    KEYSALE_RUNNING("KEYSALE.RUNNING", "&f&m------------------------------- {0} &a&lKey Sale &7(store.revampmc.eu)&7: {0}  &7& &c%time% Minutes Left {0} &f&m-------------------------------"),
    KEYSALE_ENDED("KEYSALE.ENDED", "&7&m-------------------------------- {0} &e&lKey Sale &chas ended! &7(store.revampmc.eu) {0} &7&m--------------------------------"),
    //----SALEOFF----//
    SALEOFF_RUNNING("SALEOFF.RUNNING", "&f&m------------------------------- {0} &a&lSale OFF &7(store.revampmc.eu)&7: {0}  &7& &c%time% Minutes Left {0} &f&m-------------------------------"),
    SALEOFF_ENDED("SALEOFF.ENDED", "&7&m-------------------------------- {0} &c&lSale OFF &chas ended! &7(store.revampmc.eu) {0} &7&m--------------------------------"),
    //----TIMER----//
    TIMER_MESSAGE("TIMER.MESSAGE", "&8&m------------------------------- {0} &b&l* &3&lTimers &b&l* {0} {0} &9/timer &7- &aGeneral Help command. {0} &9/timer set (player) (timer) (time) &7- &aSet a player timer. {0} &9/timer list &7- &aList of all timers. {0} &8&m-------------------------------"),
    TIMER_LIST("TIMER.LIST", "&8&m------------------------------- {0} &9&lTimers: {0}    - &b&lsnowball {0}    - &b&lblockeggdefender {0}    - &b&lblockeggshooter {0}    - &b&lswitcheregg {0}    - &b&lgrapplinghook {0}    - &b&lthrowweb {0}    - &b&lPORTABLESTRENGTH {0}    - &b&lrandomizer {0}    - &b&llumberjack {0}    - &b&lcooldownbow {0}    - &b&lcocaine {0}    - &b&ldisarmeraxe {0}    - &b&lninja {0}    - &b&lfreezegun {0}    - &b&linvisdust {0}    - &b&lbackpack {0}    - &b&lrocket {0}    - &b&lsecondchance {0}    - &b&lpyroball {0}    - &b&lpumpkinswapper {0}    - &b&lmalignantegg {0}    - &b&lenderpearl {0}    - &b&lgapple {0}    - &b&lgoldenhead {0}    - &b&lcrapple {0}    - &b&lall {0} &8&m-------------------------------"),
    //----VOTE----//
    VOTE("VOTE", "&7&m--------------------------------------- {0} &2&lVote &8» &avote.revampmc.eu {0} &7&m---------------------------------------"),
    //----STORE----//
    STORE("STORE", "&7&m--------------------------------------- {0} &6&lStore &8» &estore.revampmc.eu {0} &7&m---------------------------------------"),
    //----YOUTUBER----//
    YOUTUBER("YOUTUBER", "&7&m--------------------------------------- {0} &cYou&fTuber &cRequirements {0} &7» &e500 subscribers {0} &7» &e250 views per video {0} &7&m---------------------------------------"),
    //----FAMOUS----//
    FAMOUS("FAMOUS", "&7&m--------------------------------------- {0} &dFamous &cRequirements {0} &7» &e1000 subscribers {0} &7»&e500 views per video {0} &7&m---------------------------------------"),
    //----PARTNER----//
    PARTNER("PARTNER", "&7&m--------------------------------------- {0} &dPartner &cRequirements {0} &7» &e2000 subscribers {0} &7» &e1000 views per video {0} &7&m---------------------------------------"),
    //----COORDS----//
    COORDS("COORDS", "&7&m-------------------------------- {0} &6&lHCF &8| &e&lCoordinates %newline &7&m-------------------------------- {0}  &6&lKoths Locations {0}  &7* &9&lKoth1 &6»&7 300 | 300 &c(Overworld) {0}  &7* &9&lKoth2 &6»&7 -300 | -300 &c(Overworld) {0}  &7* &9&lKoth3 &6»&7 -300 | 300 &c(Overworld) {0}  &7* &9&lKoth4 &6»&7 300 | -300 &c(Overworld) {0}  &7* &9&lKoth5 &6»&7 124 | 64 &c(End World) {0}  &6&lMisc Locations {0}  &7* &3&lEnd Exit &6»&7 200 &c(Overworld) {0}  &7* &6&lGlowstone &6»&7 Disabled &c(Overworld) {0} &7&m--------------------------------"),
    //----BLOCK----//
    BLOCK_USAGE("BLOCK.USAGE", "&cBlock - Help Commands: {0} &c/block add (materialName) - Add Materials to your block list. {0} &c/block remove (materialName) - Remove Materials from your block list. {0} &c/block clear - Clear your current block list. {0} &c/block list - Check your block list."),
    BLOCK_COUNT("BLOCK.COUNT", "&7&m------------------------------ {0} &eYou have &f%count% &eblocks on your list. {0} &eList&7: %list%  {0} &7&m------------------------------"),
    //----STATS----//
    STATS("STATS", "&7&m------------------------------------- {0} &b%player% &9&lStats {0} {0} &b&lStatistics {0}  &7 &eCurrent Faction: &7%faction% {0}  &7 &eKills: &7%kills% {0}  &7 &eDeaths: &7%deaths% {0}  &7 &eBalance: &7%balance%$ {0} {0} &b&lOre Statistics {0}  &7 &aEmeralds: &7%emeralds% {0}  &7 &bDiamonds: &7%diamonds% {0}  &7 &eGold: &7%gold% {0}  &7 &cRedstone: &7%redstone% {0}  &7 &7Iron: &7%iron% {0}  &7 &8Coal: &7%coal% {0}  &7 &9Lapis: &7%lapis% {0} &7&m-------------------------------------"),
    //----JOIN_MESSAGE----//
    JOIN_MESSAGE("JOIN_MESSAGE", "&7&m------------------------------------------ {0} &bWelcome to RevampMC {0}  &3» &bFactions Size: &710 Men / No Allies {0}  &3» &bMap Kit: &7Protection: 1 / Sharpness: 1 {0}  &3» &bTeamspeak: &7ts.revampmc.eu {0}  &3» &bWebsite: &7www.revampmc.eu %newline &7&m------------------------------------------ {0} &m"),
    //----HELP----//
    HELP("HELP", "&7&m------------------------------------------ {0} &3&lHardcore Factions Help &7 - &bInformations about HCF {0} &7&m------------------------------------------ {0} &9Map Informations: {0} &bCurrent Map:&7 Map 1 {0} &bMap Border:&7 2000 {0} &bWarzone Until:&7 100 {0} &bEnd Portals:&7 1500, 1500 in each quadrant {0} &bEnchant Limits:&7 Protection 1, Sharpness 1, Power 4 {0}  %newline &9Helpful Commands: {0} &b/report (player) (reason) &7- Report cheaters with this command! {0} &b/request (reason) &7- Request staff assistance. {0}{0} &9Other Informations: {0} &bOfficial Teamspeak &7- &3ts.revampmc.eu {0} &bTwitter &7- &3www.twitter.com/revampmc {0} &bStore &7- &3store.revampmc.eu {0} &7&m------------------------------------------"),
    //----COOLDOWN----//
    COOLDOWN_ENDERPEARL("COOLDOWN.ENDERPEARL", "{prefix} &cYou can't use this for another &l%time%"),
    COOLDOWN_APPLE("COOLDOWN.APPLE", "{prefix} &cYou can't use this item for another &l%time%&c."),
    COOLDOWN_GAPPLE("COOLDOWN.GAPPLE", "{prefix} &cYou can't use this item for another &l%time%&c."),
    COOLDOWN_HEAD_APPLE("COOLDOWN.HEAD-APPLE", "{prefix} &cYou can't use this item for another &l%time%&c."),
    COOLDOWN_BARD_BUFF("COOLDOWN.BARD_BUFF", "{prefix} &cYou cannot use this for another &b%time% seconds."),
    COOLDOWN_ARCHER_SUGAR("COOLDOWN.ARCHER_SUGAR", "{prefix} &cYou can't use this for another %time%."),
    COOLDOWN_ARCHER_FEATHER("COOLDOWN.ARCHER_FEATHER", "{prefix} &cYou can't use this for another %time%."),
    COOLDOWN_ROGUE_SUGAR("COOLDOWN.ROGUE_SUGAR", "{prefix} &cYou can't use this for another %time%."),
    COOLDOWN_ROGUE_FEATHER("COOLDOWN.ROGUE_FEATHER", "{prefix} &cYou can't use this for another %time%."),
    COOLDOWN_RANGER_STRENGTH("COOLDOWN.RANGER_STRENGTH", "{prefix} &cYou can't use this for another %time%."),
    COOLDOWN_RANGER_FIRE("COOLDOWN.RANGER_FIRE", "{prefix} &cYou can't use this for another %time%."),
    COOLDOWN_RANGER_QUARTZ("COOLDOWN.RANGER_QUARTZ", "{prefix} &cYou can't use this for another %time%."),
    //----TABLIST----//
    TABLIST_ENABLED("TABLIST.ENABLED", "{prefix} &eYou have &aenabled &eTablist!"),
    TABLIST_DISABLED("TABLIST.DISABLED", "{prefix} &eYou have &cdisabled &eTablist!"),
    //----CUSTOM_ENCHANTS----//
    CUSTOM_ENCHANTS_ENCHANTMENT_SUCCESS("CUSTOM_ENCHANTS.ENCHANTMENT-SUCCESS", "{prefix} &aSuccessfully Applied The Enchantment To Your Item!"),
    CUSTOM_ENCHANTS_HAS_ALREADY_ENCHANT("CUSTOM_ENCHANTS.HAS_ALREADY_ENCHANT", "{prefix} &cThis Item Already Has This Enchant!"),
    CUSTOM_ENCHANTS_NOT_ENOUGH_MONEY("CUSTOM_ENCHANTS.NOT_ENOUGH_MONEY", "{prefix} &cYou Don't Have Enough &c&l&nXP&r &cTo Purchase This!"),
    CUSTOM_ENCHANTS_PURCHASE_SUCCESS("CUSTOM_ENCHANTS.PURCHASE-SUCCESS", "{prefix} &aYou Have Successfully Purchased The Item!"),


    END("", "");

    private final String path;
    private final String value;
    private final List<String> listValue;

    private final ConfigFile language = RevampHCF.getInstance().getLanguage();

    Language(String path, String value) {
        this.path = path;
        this.value = value;
        this.listValue = new ArrayList<>(Collections.singletonList(value));
    }

    public String toString() {
        Replacement replacement = new Replacement(CC.translate(language.getString(this.path)));
        replacement.add("{prefix} ", language.getString("PREFIX"));
        return replacement.toString().replace("{0}", "\n");
    }
}
