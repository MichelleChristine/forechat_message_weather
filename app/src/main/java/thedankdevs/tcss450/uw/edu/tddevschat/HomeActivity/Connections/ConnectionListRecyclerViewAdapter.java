package thedankdevs.tcss450.uw.edu.tddevschat.HomeActivity.Connections;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import thedankdevs.tcss450.uw.edu.tddevschat.HomeActivity.Connections.ConnectionListFragment.OnListFragmentInteractionListener;
import thedankdevs.tcss450.uw.edu.tddevschat.HomeActivity.Connections.content.Connection;
import thedankdevs.tcss450.uw.edu.tddevschat.R;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Connection} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 *
 * @author Michelle Brown, Bryan Santos
 * @version 11/17/2018
 */
public class ConnectionListRecyclerViewAdapter extends RecyclerView.Adapter<ConnectionListRecyclerViewAdapter.ViewHolder> {

    private List<Connection> mConnections;
    private final List<Connection> mCopyConnections;
    private final OnListFragmentInteractionListener mListener;

    public ConnectionListRecyclerViewAdapter(List<Connection> items, OnListFragmentInteractionListener listener) {
        mConnections = items;
        mListener = listener;
        mCopyConnections = new ArrayList<>();
        mCopyConnections.addAll(mConnections);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_connections, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = (mConnections).get(position);
        holder.mUsername.setText(mConnections.get(position).getUsername());

        try {
            if (!(holder.mItem.getIsMine())) {
                holder.mUsername.setTextColor(Color.GRAY);
            }
        } catch (Exception e) {
            Log.e("CONNECTION VIEW HOLDER", "my isMine value is weird!! " + e);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    /* Notify the active callbacks interface (the activity, if the
                       fragment is attached to one) that an item has been selected.*/
                    mListener.onConnectionsListFragmentInteraction(holder.mItem);
                }
            }
        });
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != mListener) {
                    mListener.onConnectionsListFragmentLongInteraction(holder.mItem);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mConnections.size();
    }

    /**
     * Initially clears the list currently being displayed and
     * rebuilds the list based on the text query passed in matched up with
     * the original list (stored in mCopyConnections)
     *
     * @param text search query
     * @return true if at least one field of a Connection object contains the character
     * sequence of the text passed in
     */

    public boolean filter(String text) {

        /*
            Clearing the list each time is needed because of the way that the logic
            is set up. Since the loop only checks the copyConnections list if it contains
            the character sequence of text, then it adds it to the list. Without
            clearing the list initially, the current logic would add duplicate Connections
            in mConnections.
         */

        mConnections.clear();
        if (text.isEmpty()) {
            mConnections.addAll(mCopyConnections);
            return true;
        }

        for (Connection c : mCopyConnections) {
            if (containsText(c, text)) {
                mConnections.add(c);
            }
        }

        notifyDataSetChanged(); // refresh the adapter with the new list

        /*
            Returning false meant that mCopyConnections (which was the list
            initially passed in does not contain the character sequence of text).
         */

        return !mConnections.isEmpty();
    }

    /**
     * Helper method to check if character sequence of the text passed in
     * is found in one of the fields of Connection object. Note that the
     * character sequence doesn't have to be an exact match, one of the fields
     * just needs to contain parts of it in sequence.
     *
     * @param c    Connection object currently being examined
     * @param text search query
     * @return
     */
    private boolean containsText(Connection c, String text) {

        return c.getFirstName().toLowerCase().contains(text) ||
                c.getLastName().toLowerCase().contains(text) ||
                c.getUsername().toLowerCase().contains(text) ||
                c.getEmail().toLowerCase().contains(text);
    }

    /**
     * The initial list passed in that will remain unchanged (i.e. mCopyConnections)
     * is used as the field to check for equality.
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RecyclerView.Adapter)) {
            return false;
        }

        ConnectionListRecyclerViewAdapter theOther = (ConnectionListRecyclerViewAdapter) obj;
        return mConnections.equals(theOther.mCopyConnections);
    }

    /**
     * Hashes the field mCopyConnections to complement the overriden equals() method.
     *
     * @return hash of mCopyConnections
     */
    @Override
    public int hashCode() {
        return Objects.hash(mCopyConnections);
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mUsername;
        public Connection mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mUsername = view.findViewById(R.id.tv_connectionslist_username);
        }

    }
}
