package pt.chat.xmpp;

import pt.chat.entities.Account;
import pt.chat.xmpp.stanzas.MessagePacket;

public interface OnMessagePacketReceived extends PacketReceived {
	public void onMessagePacketReceived(Account account, MessagePacket packet);
}
