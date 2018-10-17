package com.acmr.test;

import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import com.acmr.dao.security.ISecurityDao;
import com.acmr.dao.security.SecurityDao;
import com.acmr.dao.zhzs.IndexListDao;
import com.acmr.model.pub.PageBean;
import com.acmr.model.security.Department;
import com.acmr.model.security.Role;
import com.acmr.model.security.User;
import com.acmr.service.security.DepartmentService;
import com.acmr.service.security.RoleService;
import com.acmr.service.security.UserService;

import java.util.*;

public class Mytest {
    public static void main(String[] args) {
        String s="{}";
        PubInfo.printStr(String.valueOf(isValid(s)));
    }
    public static boolean isValid(String s) {
        List<Map<Character, Character>> mapping=new ArrayList<>();


        return true;
    }








    //链表
    static class ListNode{
        int val;
        ListNode next;
        ListNode(int x){
            val=x;
            next=null;
        }
    }
    //删除node
    public static void deleteNode(ListNode node) {
        node.val=node.next.val;
        node.next=node.next.next;
    }
    //判断链表是否为回文链表
    public static boolean isPalindrome(ListNode head) {
        ListNode mid=middleNode(head);
        ListNode head1=reverseList(mid);
        while (head!=null&&head!=mid){
            if (head.val==head1.val){
                head=head.next;
                head1=head1.next;
            }
            else return false;
        }
        return true;
    }

    //返回链表中间节点，两点中间就返回第二个
    public static ListNode middleNode(ListNode head) {
        //快慢指针法
        if (head==null||head.next==null) return head;
        ListNode p1=head;
        ListNode p2=p1.next;
        while (p2!=null){
            p1=p1.next;
            if(p2.next!=null){

                p2=p2.next.next;
            }
            else{
                return p1;
            }
        }


        return p1;


        //笨方法
        /*int length=0;
        ListNode node=head;
        while (node!=null){
            length++;
            node=node.next;
        }
        int middle=length/2+1;
        node=head;
        while (node!=null){
            middle--;
            if (middle==0){
                return node;
            }
            else node=node.next;
        }

        return head;*/
    }

    //删除链表中指定的值

    public static ListNode removeElements(ListNode head, int val) {
        if (head==null) return head;
        while (head.val==val){
            head=head.next;
            if (head==null) return head;
        }
        ListNode prev=head;
        ListNode node=prev.next;
        if (node!=null){
            while (node.next!=null){
                if (node.val==val){
                    prev.next=node.next;
                    node=node.next;
                }
                else {
                    prev=node;
                    node=node.next;
                }
            }
            if (node.val==val){
                prev.next=null;
            }
        }
        return head;
    }


    //找出两个链表相交的点
    public static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if(headA==null||headB==null) return null;
        if(headA==headB) return headA;
        ListNode bottom=null;
        if(headA.next!=null){
            bottom=headA.next;
            while(bottom.next!=null){
                bottom=bottom.next;
            }
        }
        else{
            bottom=headA;
        }
        bottom.next=headB;
        ListNode node=hasCycle(headA);
        bottom.next=null;
        return node;
    }


    //判断是否成环,且返回环入口点，如果没有环返回空
    public static ListNode hasCycle(ListNode head){
        Boolean re=null;
        if (head!=null&&head.next!=null){
            ListNode p1=head.next;
            ListNode p2=head.next.next;
            while (p2!=null){
                if (p1!=p2&&p2.next!=null){
                    p1=p1.next;
                    p2=p2.next.next;
                }
                else if (p2.next==null){
                    return null;
                }
                else {
                    //成环，返回入口点
                    ListNode p_1=head;
                    ListNode p_2=p2;
                    while (p_1!=p_2){
                        p_1=p_1.next;
                        p_2=p_2.next;
                    }
                    return p_2;
                }
            }
        }

        return null;
    }

    //反转
    public static ListNode reverseList(ListNode head) {

        if (head.next!=null){
            ListNode headnode=reverseList(head.next);
            ListNode next=head.next;
            next.next=head;
            head.next=null;
            return headnode;
            //headnode.next=head;
            //head.next=null;
        }
        else {
            return head;
        }
    }



}
