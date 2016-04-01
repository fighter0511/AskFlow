package pt.chat.xmpp;

import pt.chat.entities.Account;
import pt.chat.xmpp.stanzas.IqPacket;

public interface OnIqPacketReceived extends PacketReceived {
	public void onIqPacketReceived(Account account, IqPacket packet);
}
