package com.masst.memo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Dell on 1/19/2018.
 */

public class  MemoRVAdapter extends RecyclerView.Adapter<MemoRVAdapter.MemoRVViewHolder> {

    private Context context;
    private ArrayList<Memo> alMemos;
    private LayoutInflater layoutInflater;
    private DatabaseAccess databaseAccess;
    Snackbar sb;
    public View snackview;

    public MemoRVAdapter (Context context, ArrayList<Memo> alMemos) {
        this.context = context;
        this.alMemos = alMemos;
        this.layoutInflater = LayoutInflater.from(this.context);
        databaseAccess = DatabaseAccess.getInstance(context);
    }

    @Override
    public MemoRVViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.layout_list_item, parent,false);
        MemoRVViewHolder memoRVViewHolder = new MemoRVViewHolder(itemView);
        return memoRVViewHolder;
    }

    @Override
    public void onBindViewHolder (MemoRVViewHolder holder, final int position) {

        final Memo memo = alMemos.get(position);
        memo.setFullDisplayed(false);

        holder.tvDate.setText(memo.getDate());
        holder.tvText.setText(memo.getShortText());
        holder.tvText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewActivity.class);
                String text=memo.getShortText();
                String date =memo.getDate();
                Bundle bundle = new Bundle();
                bundle.putString("memo_text",text);
                bundle.putString("memo_date",date);
                intent.putExtras(bundle);
                if(!text.equals(null) && !date.equals(null)) {
                    context.startActivity(intent);
                }
                else
                {
                    sb = Snackbar.make(snackview,"Please fill the data",Snackbar.LENGTH_LONG);
                    sb.setAction("Exit", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sb.dismiss();
                        }
                    });
                }
            }
        });
        holder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Delete Memo");
                alertDialog.setMessage("Are You Sure you Want to Delete?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeItem(alMemos.get(position), position);
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();

            }
        });
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditMemoActivity.class);
                Bundle bundle = new Bundle();
                String text=memo.getShortText();
                String date =memo.getDate();
                bundle.putString("memo_text",text);
                bundle.putString("memo_date",date);
                intent.putExtras(bundle);
                if(!text.equals(null) && !date.equals(null)) {
                    context.startActivity(intent);
                }
                else
                {
                    sb = Snackbar.make(snackview,"Please fill the data",Snackbar.LENGTH_LONG);
                    sb.setAction("Exit", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sb.dismiss();
                        }
                    });
                }
            }
        });

    }
    @Override
    public int getItemCount () {
        return alMemos.size();
    }

    public void updateData(ArrayList<Memo> viewModels) {
        alMemos.clear();
        alMemos.addAll(viewModels);
        notifyDataSetChanged();
    }

    public void insertItem (int position,Memo memo) {
        alMemos.add(position, memo);
        notifyItemInserted(position);
    }

    public void removeItem (Memo memo, int position) {
        alMemos.remove(memo);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());

        //remove from db
        databaseAccess.open();
        databaseAccess.delete(memo);
        databaseAccess.close();
    }

    public class MemoRVViewHolder extends RecyclerView.ViewHolder {
        private TextView tvText;
        private TextView tvDate;
        private ImageView ivDel;
        private ImageView ivEdit;

        public MemoRVViewHolder (View itemView) {
            super(itemView);

            tvDate = (TextView) itemView.findViewById(R.id.txtDate);
            tvText = (TextView) itemView.findViewById(R.id.txtMemo);
            ivDel = (ImageView) itemView.findViewById(R.id.btnDelete);
            ivEdit = (ImageView) itemView.findViewById(R.id.btnEdit);
        }
    }
}
