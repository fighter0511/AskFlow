package pt.chat.xmpp;

import pt.chat.entities.Account;
import pt.chat.xmpp.stanzas.PresencePacket;

public interface OnPresencePacketReceived extends PacketReceived {
	public void onPresencePacketReceived(Account account, PresencePacket packet);
}
