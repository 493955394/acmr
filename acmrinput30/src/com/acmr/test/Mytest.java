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

    }



    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        int[] array=new int[1000];

        return array;
    }

    //用栈实现队列
    class MyQueue {

        /** Initialize your data structure here. */
        Stack stack=new Stack();
        public MyQueue() {

        }

        /** Push element x to the back of queue. */
        public void push(int x) {
            this.stack.push(x);
        }

        /** Removes the element from in front of queue and returns that element. */
        public int pop() {
            Stack s=new Stack();
            while (!this.stack.isEmpty()){
                s.push(this.stack.pop());
            }
            int x= (int) s.pop();
            while (!s.isEmpty()){
                this.stack.push(s.pop());
            }
            return x;
        }

        /** Get the front element. */
        public int peek() {
            Stack s=new Stack();
            while (!this.stack.isEmpty()){
                s.push(this.stack.pop());
            }
            int x= (int) s.pop();
            s.push(x);
            while (!s.isEmpty()){
                this.stack.push(s.pop());
            }
            return x;

        }

        /** Returns whether the queue is empty. */
        public boolean empty() {
            if (this.stack.isEmpty()) return true;
            else return false;
        }
    }

    //用队列实现栈
    static class MyStack {

        /** Initialize your data structure here. */
        Queue<Integer>  queue=new LinkedList<>();
        public MyStack() {
        }

        /** Push element x onto stack. */
        public void push(int x) {
            queue.add(x);
        }

        /** Removes the element on top of the stack and returns that element. */
        public int pop() {
            int p=0;
            Queue<Integer> q=new LinkedList<>();
            while (!this.queue.isEmpty()){
                int x=queue.poll();
                if(!this.queue.isEmpty())q.add(x);
                else p=x;
            }
            while (!q.isEmpty()){
                this.queue.add(q.poll());
            }
            return p;
        }

        /** Get the top element. */
        public int top() {
            int x=this.pop();
            this.push(x);
            return x;
        }

        /** Returns whether the stack is empty. */
        public boolean empty() {
            if (this.queue.isEmpty()) return true;
            else return false;
        }
    }


    //最小元素栈
    static class MinStack {

        /** initialize your data structure here. */
        Stack stack;
        int min=0;
        public MinStack() {
            this.stack=new Stack();
        }

        public void push(int x) {
            if(this.stack.size()==0) this.min=x;
            this.stack.push(x);
            if (this.min>x) this.min=x;
        }

        public void pop() {
            int x= (int) stack.pop();
            if (x==this.min&&this.stack.size()>0){
                Stack s=new Stack();
                this.min= (int) this.stack.pop();
                s.push(this.min);
                while (this.stack.size()!=0){
                    int y= (int) this.stack.pop();
                    s.push(y);
                    if (this.min>y) this.min=y;
                }
                while (s.size()!=0){
                    this.stack.push(s.pop());
                }
            }
            else if (this.stack.size()==0) this.min=0;
        }

        public int top() {
            int x=(int)this.stack.pop();
            this.stack.push(x);
            return x;
        }

        public int getMin() {
            return this.min;
        }
    }

    //括号是否合法
    public static boolean isValid(String s) {
        if (s.length()==0) return true;
        Map<String, String> mapping=new HashMap<>();
        mapping.put("{","}");
        mapping.put("(",")");
        mapping.put("[","]");
        List<String> list=Arrays.asList(s.split(""));
        list=list.subList(1,list.size());
        Stack stack=new Stack();
        for (int i=0;i<list.size();i++){
            if (i==0&&!mapping.containsKey(list.get(i))) return false;
            //else if (i==list.size()-1&&mapping.containsKey(list.get(i))) return false;
            else if (mapping.containsKey(list.get(i))&&i!=list.size()-1){
                stack.push(list.get(i));
            }
            else {
                if (stack.size()==0) return false;
                else {
                    String thiss= String.valueOf(stack.pop());
                    if (!mapping.get(thiss).equals(list.get(i))) return false;
                }
            }
        }
        if (stack.size()!=0) return false;
        else return true;
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
