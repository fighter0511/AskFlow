package pt.askflow.xmpp.jid;


import pt.askflow.Config;

public final class Xmlns {
	public static final String BLOCKING = "urn:xmpp:blocking";
	public static final String ROSTER = "jabber:iq:roster";
	public static final String TOPIC = "anyquiz:iq:topic";
	public static final String PENDING_QUESTIONS = "anyquiz:iq:quiz_pending";
	public static final String REGISTER = "jabber:iq:register";
	public static final String BYTE_STREAMS = "http://jabber.org/protocol/bytestreams";
	public static final String HTTP_UPLOAD = Config.LEGACY_NAMESPACE_HTTP_UPLOAD ? "eu:siacs:conversations:http:upload" : "urn:xmpp:http:upload";
	public static final String ASK_QUESTION = "anyquiz:iq:quiz_ask";
	public static final String QUIZ_STATUS = "anyquiz:iq:quiz_status";
	public static final String QUIZ_GUIDER = "anyquiz:iq:quiz_guider";
	public static final String QUIZ_CANCEL = "anyquiz:iq:quiz_cancel";
}
