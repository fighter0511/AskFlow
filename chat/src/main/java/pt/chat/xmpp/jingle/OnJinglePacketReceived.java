package pt.chat.xmpp.jingle;

import pt.chat.entities.Account;
import pt.chat.xmpp.PacketReceived;
import pt.chat.xmpp.jingle.stanzas.JinglePacket;

public interface OnJinglePacketReceived extends PacketReceived {
	void onJinglePacketReceived(Account account, JinglePacket packet);
}
