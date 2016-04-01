package pt.chat.xmpp;

import pt.chat.entities.Account;

public interface OnStatusChanged {
	public void onStatusChanged(Account account);
}
