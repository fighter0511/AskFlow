package pt.askflow;

/**
 * Created by Phuc on 7/18/15.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnItemTouchListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.IQTypeFilter;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import pt.askflow.util.Variables;
import pt.stickerlibrary.ChatAdapter;
import pt.stickerlibrary.MessageItem;
import pt.stickerlibrary.ScrollingHandler;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the
 * interface.
 */
public class ChatFragment extends Fragment implements AbsListView.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private ChatAdapter mChatAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<MessageItem> arrayList = new ArrayList<>();
    private boolean mNoConversation;
    private boolean mIsClearNoConversation;
    private int mPreviousPositionItemClick = -1;
    private XMPPConnection connection;
    private String contact;
    private String username;
    private String password;

    // TODO: Rename and change types of parameters
    public static ChatFragment newInstance(String contact) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString("contact", contact);
        fragment.setArguments(args);
        return fragment;
    }

    public ChatFragment() {
    }

    private void createConnection(){
        ConnectionConfiguration config = new ConnectionConfiguration(Variables.HOST, Variables.PORT, Variables.SERVICE);
        connection = new XMPPConnection(config);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    connection.connect();
                } catch (XMPPException e) {
                    e.printStackTrace();
                    Log.d("xxx", "Chat activity connect error");
                }

                try {
                    connection.login(username, password);
                } catch (XMPPException e) {
                    e.printStackTrace();
                    Log.d("xxx", "Chat activity login error");
                }
                if(connection != null){
                    PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
                    PacketFilter resultIQ = new IQTypeFilter(IQ.Type.RESULT);
                    PacketFilter setIQ = new IQTypeFilter(IQ.Type.GET);
                    PacketFilter getIQ = new IQTypeFilter(IQ.Type.SET);
                    PacketFilter errorIQ = new IQTypeFilter(IQ.Type.ERROR);
                    connection.addPacketListener(new PacketListener() {
                        @Override
                        public void processPacket(Packet packet) {
                            Log.d("uuuResult", packet.toString());
                        }
                    }, resultIQ);
                    connection.addPacketListener(new PacketListener() {
                        @Override
                        public void processPacket(Packet packet) {
                            Log.d("uuuSet", packet.toString());
                        }
                    }, setIQ);
                    connection.addPacketListener(new PacketListener() {
                        @Override
                        public void processPacket(Packet packet) {
                            Log.d("uuuGet", packet.toString());
                        }
                    }, getIQ);
                    connection.addPacketListener(new PacketListener() {
                        @Override
                        public void processPacket(Packet packet) {
                            Log.d("uuuError", packet.toString());
                        }
                    }, errorIQ);
                    connection.addPacketListener(new PacketListener() {
                        @Override
                        public void processPacket(Packet packet) {
                            Message messageReceived = (Message) packet;
                            if(messageReceived.getBody() != null){
                                String fromName = StringUtils.parseBareAddress(messageReceived.getFrom());
                                String body = messageReceived.getBody();
                                MessageItem item = new MessageItem();

                                if(body.contains("R.drawable")){
                                    String stickerName = body.replace("R.drawable", "");
                                    int id = Integer.parseInt(stickerName);
                                    Log.d("xxx", "rev" + id);
                                    item.setMessageType(MessageItem.TYPE_FRIEND_STICKER);
                                    item.setStickerUrl(id);
                                } else {
                                    item.setMessageType(MessageItem.TYPE_FRIEND);
                                    item.setMessageText(body);
                                }
                                arrayList.add(item);
                                mRecyclerView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mChatAdapter.notifyDataSetChanged();
                                        mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount());
                                    }
                                }, 100);
                            }
                        }
                    }, filter);

                }
            }
        });
        thread.start();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contact = getArguments().getString("contact");
        SharedPreferences sp = getActivity().getSharedPreferences(Variables.ACCOUNT, Context.MODE_PRIVATE);
        username = sp.getString(Variables.USEER_NAME, "");
        password = sp.getString(Variables.PASSWORD, "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        //
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
//        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        int duration = 1000;
        ScrollingHandler mLayoutManager = new ScrollingHandler(getActivity(), LinearLayoutManager.VERTICAL, false, duration);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)

        mChatAdapter = new ChatAdapter(arrayList);

        mChatAdapter.setOnChatScreenClickListener(new ChatAdapter.OnClickChatScreenListener() {
            @Override
            public void onErrorMessageClick(View view) {
            }

            @Override
            public void onMessageClick(View view, int position) {
                invisiblePreviousMessageStatus(position);
                MessageItem item = arrayList.get(position);
                if (item != null) {
                    boolean isSet = item.getVisibilityDate();
                    item.setVisibilityDate(!isSet);
                    mChatAdapter.notifyDataSetChanged();
                }
                mPreviousPositionItemClick = position;
            }

            @Override
            public void onStickerMessageClick(View view, int position) {
                invisiblePreviousMessageStatus(position);
                MessageItem item = arrayList.get(position);
                if (item != null) {
                    boolean isSet = item.getVisibilityDate();
                    item.setVisibilityDate(!isSet);
                    mChatAdapter.notifyDataSetChanged();
                }
                mPreviousPositionItemClick = position;
            }
        });


        mRecyclerView.setAdapter(mChatAdapter);
        mRecyclerView.scrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
        mRecyclerView.addOnItemTouchListener(new OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.orange1, R.color.green1, R.color.blue1);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        setupAdapter();
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                }, 2500);
            }
        });

        //set connection

        createConnection();
        super.onViewCreated(view, savedInstanceState);
    }

    private void invisiblePreviousMessageStatus(int currentPos) {
        if (currentPos != mPreviousPositionItemClick &&
                mPreviousPositionItemClick != -1 && arrayList.size() > mPreviousPositionItemClick) {
            MessageItem previousItem = arrayList.get(mPreviousPositionItemClick);
            if (previousItem != null)
                previousItem.setVisibilityDate(false);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void sendMessage(String message) {
        if (mNoConversation && !mIsClearNoConversation) {
            mIsClearNoConversation = true;
            arrayList.clear();
        }

        if (arrayList.size() > 0) {
            arrayList.get(arrayList.size() - 1).setVisibilityStatus(false);
        }
        //send to server
        Message messageSent = new Message(contact, Message.Type.chat);
        messageSent.setBody(message);
        connection.sendPacket(messageSent);

        //update ui
        MessageItem item = new MessageItem();
        item.setMessageType(MessageItem.TYPE_MYSELF);
        item.setMessageText(message);
        item.setIsWarning(true);
        item.setVisibilityStatus(true);
        arrayList.add(item);

        mChatAdapter.notifyDataSetChanged();
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount());
            }
        }, 100);
    }

    public void sendMessage(boolean isSticker, boolean isMe, int i) {
        if (mNoConversation && !mIsClearNoConversation) {
            mIsClearNoConversation = true;
            arrayList.clear();
        }

        if (arrayList.size() > 0) {
            arrayList.get(arrayList.size() - 1).setVisibilityStatus(false);
        }

        MessageItem item = new MessageItem();
        if (isSticker && isMe) {
            Message messageSent = new Message(contact, Message.Type.chat);
            messageSent.setBody(i + "R.drawable");
            connection.sendPacket(messageSent);

            item.setIsWarning(false);
            item.setMessageType(MessageItem.TYPE_MYSELF_STICKER);
            item.setStickerUrl(i);
        } else {
            if (isSticker) {
                item.setMessageType(MessageItem.TYPE_FRIEND_STICKER);
            }
        }
        item.setVisibilityStatus(true);
        arrayList.add(item);

        mChatAdapter.notifyDataSetChanged();
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount());
            }
        }, 100);
    }

    public void scrollToLast() {
        mRecyclerView.scrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
    }

    @Override
    public void onResume() {
        super.onResume();
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount());
            }
        }, 100);
    }
}

