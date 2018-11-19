package com.acmr.test;

import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import com.acmr.dao.security.ISecurityDao;
import com.acmr.dao.security.SecurityDao;
import com.acmr.dao.sysindex.IFlowIndexDao;
import com.acmr.dao.zhzs.IndexListDao;
import com.acmr.model.pub.PageBean;
import com.acmr.model.security.Department;
import com.acmr.model.security.Role;
import com.acmr.model.security.User;
import com.acmr.model.zhzs.IndexList;
import com.acmr.service.security.DepartmentService;
import com.acmr.service.security.RoleService;
import com.acmr.service.security.UserService;
import com.lowagie.text.Paragraph;
import com.sun.org.apache.regexp.internal.RE;
import org.omg.PortableServer.LIFESPAN_POLICY_ID;
import sun.reflect.generics.tree.Tree;

import javax.swing.table.TableRowSorter;
import javax.transaction.TransactionRequiredException;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Mytest {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

    }



    ///哈希表

    //快乐数
    public boolean isHappy(int n) {
        HashSet set=new HashSet();
        List<Integer> list=new ArrayList<>();
        list.add(n%10);
        return false;
    }





    ///树
    public static class TreeNode implements Serializable{
        private static final long serialVersionUID = 1L;
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x){val=x;}
        //深拷贝
        public Object deepClone() throws IOException, ClassNotFoundException {
            ByteArrayOutputStream bo=new ByteArrayOutputStream();
            ObjectOutputStream oo=new ObjectOutputStream(bo);
            oo.writeObject(this);
            ByteArrayInputStream bi=new ByteArrayInputStream(bo.toByteArray());
            ObjectInputStream oi=new ObjectInputStream(bi);
            return oi.readObject();

        }
    }

    public static class Node {
        public int val;
        public List<Node> children;

        public Node() {}

        public Node(int _val,List<Node> _children) {
            val = _val;
            children = _children;
        }
    }


    //按递增顺序查找的树
    public static TreeNode increasingBST(TreeNode root) {
        List<Integer> list=getDfsList(root);
        for (Integer i:list){
            PubInfo.printStr(String.valueOf(i));
        }
        if (list.size()==0) return null;
        TreeNode node=new TreeNode(list.get(0));
        TreeNode thisnode=node;
        for (int i=1;i<list.size();i++){
            TreeNode newnode=new TreeNode(list.get(i));
            thisnode.right=newnode;
            thisnode=newnode;
        }
        return node;
    }
    public static List<Integer> getDfsList(TreeNode root){
        List<Integer> list=new ArrayList<>();
        if (root!=null){
            list.addAll(getDfsList(root.left));
            list.add(root.val);
            list.addAll(getDfsList(root.right));
        }
        return list;
    }
    //叶子相似的树
    public boolean leafSimilar(TreeNode root1, TreeNode root2) {
        List<Integer> list1=getLeaf(root1);
        List<Integer> list2=getLeaf(root2);
        if (list1.size()!=list2.size()) return false;
        for (int i=0;i<list1.size();i++){
            if (list1.get(i)!=list2.get(i)) return false;
        }
        return true;
    }
    public List<Integer> getLeaf(TreeNode root){
        List<Integer> list=new ArrayList<>();
        if (root!=null){
            if (root.left==null&&root.right==null){
                list.add(root.val);
            }
            else {
                list.addAll(getLeaf(root.left));
                list.addAll(getLeaf(root.right));
            }
        }
        return list;
    }

    //二叉搜索树中的搜索
    public TreeNode searchBST(TreeNode root, int val) {
        TreeNode node=null;
        if (root==null) return node;
        int v=root.val;
        if (val==v){
            node=root;
        }
        else if (val<v){
            node=searchBST(root.left,val);
        }
        else {
            node=searchBST(root.right,val);
        }
        return node;
    }


    //二叉树中第二小节点
    Integer re=null;
    public int findSecondMinimumValue(TreeNode root) {
        if (re==null) re=root.val;
        int val=root.val;
        if (val>re){
            return val;
        }
        else if (root.left!=null){
            if (findSecondMinimumValue(root.left)==-1&&findSecondMinimumValue(root.right)==-1){
                return -1;
            }
            else if (findSecondMinimumValue(root.left)!=-1&&findSecondMinimumValue(root.right)!=-1){
                return Math.min(findSecondMinimumValue(root.left),findSecondMinimumValue(root.right));
            }
            else {
                return findSecondMinimumValue(root.left)>findSecondMinimumValue(root.right)?findSecondMinimumValue(root.left):findSecondMinimumValue(root.right);
            }
        }
        else return -1;
    }

    //修剪二叉搜索树
    public TreeNode trimBST(TreeNode root, int L, int R) {
        if (root==null) return null;
        int val=root.val;
        if (val<L){
            root=trimBST(root.right,L,R);
        }
        else if (val>R){
            root=trimBST(root.left,L,R);
        }
        else {
            root.left=trimBST(root.left,L,R);
            root.right=trimBST(root.right,L,R);
        }
        return root;
    }

    //两数之和，BST,二叉搜索树
    public boolean findTarget(TreeNode root, int k) {
        List<Integer> list=getBfsList(root);
        for (int i=0;i<list.size();i++){
            int n=k-list.get(i);
            for (int j=i+1;j<list.size();j++){
                if (list.get(j)==n) return true;
            }
        }
        return false;
    }

    //二叉树坡度
    public int findTilt(TreeNode root) {
        int tilt=0;
        if (root!=null){
            tilt=Math.abs(getSum(root.left)-getSum(root.right))+findTilt(root.left)+findTilt(root.right);
        }
        return tilt;
    }
    public int getSum(TreeNode root){
        int sum=0;
        if (root!=null){
            sum=root.val+getSum(root.left)+getSum(root.right);
        }
        return sum;
    }

    //二叉树层平均值
    public List<Double> averageOfLevels(TreeNode root) {
        List<Double> list=new ArrayList<>();
        List<List<Integer>> lists=levelOrderBottom1(root);
        for (List<Integer> thislist:lists){
            Double sum= Double.valueOf(0);
            for (Integer integer:thislist){
                sum=sum+integer;
            }
            Double avg=sum/thislist.size();
            list.add(avg);
        }
        return list;
    }

    //根据二叉树创建字符串
    public String tree2str(TreeNode t) {
        String ostring=getString(t);
        return null;
    }
    public String getString(TreeNode t){
        if (t==null) return null;
        else {
            if (t.left==null&&t.right!=null){
                return t.val+"()"+"("+getString(t.right)+")";
            }
            else if (t.left!=null&&t.right==null){
                return t.val+"("+getString(t.left)+")";
            }
            else if (t.left!=null&&t.right!=null){
                return t.val+"("+getString(t.left)+")"+"("+getString(t.right)+")";
            }
            else {
                return String.valueOf(t.val);
            }
        }
    }


    //N叉树的最大深度
    public int NmaxDepth(Node root) {
        int dep=0;
        if (root==null) return dep;
        int max=0;
        for (int i=0;i<root.children.size();i++){
            max=Math.max(NmaxDepth(root.children.get(i)),max);
        }
        dep=dep+max+1;

        return dep;
    }


    //二叉搜索树转为累加树
    public TreeNode convertBST(TreeNode root) {
        List<Integer> list=getBfsList(root);
        addVal(root,list);
        return root;
    }
    public void addVal(TreeNode root,List<Integer> list){
        if (root==null) return;
        addVal(root.left,list);
        addVal(root.right,list);
        int val=root.val;
        for (Integer integer:list){
            if (val<integer){
                root.val=root.val+integer;
            }
        }
    }
    public List<Integer> getBfsList(TreeNode root){
        List<Integer> list=new ArrayList<>();
        if (root==null) return list;
        else {
            list.addAll(getBfsList(root.right));
            list.add(root.val);
            list.addAll(getBfsList(root.left));
        }
        return list;
    }

    //二叉搜索树中的众数
    static Integer pre=null;
    static int max=0;
    static int count=1;
    static List<Integer> list=new ArrayList<>();
    public static int[] findMode(TreeNode root) {
        dfs(root);
        if (list.size()==0){
            if (root==null) {
                int[] a={};
                return a;
            }
            int[] re={root.val};
            return re;
        }
        Integer[] b=new Integer[list.size()];
        Integer[] a=  list.toArray(b);
        int[] re=new int[list.size()];
        for (int i=0;i<list.size();i++){
            re[i]=a[i];
        }
        return re;
    }
    //中序遍历二叉树
    public static void dfs(TreeNode root){
        if (root==null) return;
        dfs(root.left);
        if (pre!=null){
            if (pre==root.val){
                count++;
            }
            else {
                count=1;
            }
            if (count>max){
                list.clear();
                list.add(root.val);
                max=count;
            }
            else if (count==max){
                list.add(root.val);
            }
        }
        else {
            list.add(root.val);
            max=1;
        }
        pre=root.val;
        dfs(root.right);
    }



    //路径总和
    public static int pathSum(TreeNode root, int sum) {
        int num=0;
        if (root!=null){
            num=num+hasPathSum1(root,sum)+pathSum(root.left,sum)+pathSum(root.right,sum);
        }
        return num;
    }
    public static  int hasPathSum1(TreeNode root, int sum) {
        int nums=0;
        if (root==null) return nums;
        int surplus=sum-root.val;
        if (surplus==0) {
            nums=1+hasPathSum1(root.left,0)+hasPathSum1(root.right,0);
        }
        else {
            return hasPathSum1(root.left,surplus)+hasPathSum1(root.right,surplus);
        }
        return nums;
    }

    //二叉树的层次遍历
    public List<List<Integer>> levelOrderBottom(TreeNode root) {
        List<List<Integer>> lists=levelOrderBottom1(root);
        Collections.reverse(lists);
        return lists;
    }

    public List<List<Integer>> levelOrderBottom1(TreeNode root){
        List<List<Integer>> lists=new ArrayList<>();
        Stack<List<Integer>> stack=new Stack<>();
        if (root!=null){
            List<Integer> list=new ArrayList<>();
            list.add(root.val);
            lists.add(list);
            //stack.push(list);
            if (root.left!=null||root.right!=null){
                List<List<Integer>> leftlist=levelOrderBottom1(root.left);
                List<List<Integer>> rightlist=levelOrderBottom1(root.right);
                for (int i=0;i<leftlist.size();i++){
                    if (lists.size()<i+2){
                        lists.add(leftlist.get(i));
                        //stack.push(leftlist.get(i));
                    }
                    else {
                        lists.get(i+1).addAll(leftlist.get(i));

                    }
                }
                for (int i=0;i<rightlist.size();i++){
                    if (lists.size()<i+2){
                        lists.add(rightlist.get(i));
                    }
                    else {
                        lists.get(i+1).addAll(rightlist.get(i));
                    }
                }

            }
        }
        return lists;
    }

    //N叉树的层序遍历
    public List<List<Integer>> levelOrder(Node root) {
        List<List<Integer>> lists=new ArrayList<>();
        if (root==null) return lists;
        List<Integer> list=new ArrayList<>();
        list.add(root.val);
        lists.add(list);
        if (root.children.size()!=0){
            for (Node node:root.children){
                List<List<Integer>> nodelist=levelOrder(node);
                for (int i=0;i<nodelist.size();i++){
                    int index=i+1;
                    if (lists.size()<index+1){
                        lists.add(nodelist.get(i));
                    }
                    else {
                        lists.get(index).addAll(nodelist.get(i));
                    }
                }
            }
        }
        return lists;
    }
    //N叉树的后序遍历
    public List<Integer> postorder(Node root) {
        List<Integer> list=new ArrayList<>();
        if (root!=null){
            for (Node node:root.children){
                list.addAll(postorder(node));
            }
            list.add(root.val);
        }
        return list;
    }

    //N叉树的前序遍历
    public List<Integer> preorder(Node root) {
        List<Integer> list=new ArrayList<>();
        if (root!=null){
            list.add(root.val);
            for (Node node :root.children){
                list.addAll(preorder(node));
            }
        }
        return list;
    }

    //另一个树的子树
    public static boolean isSubtree(TreeNode s, TreeNode t) {
        if (s==null&&t==null)return true;
        else if (s==null^t==null) return false;
        if (isSameTree(s,t)) return true;
        else {
            return isSubtree(s.left,t)||isSubtree(s.right,t);
        }
    }
    //路径总和
    public static boolean hasPathSum(TreeNode root, int sum) {
        if (root==null) return false;
        int surplus=sum-root.val;
        if (surplus==0) {
            if (root.left==null&&root.right==null) return true;
            else return hasPathSum(root.left,surplus)||hasPathSum(root.right,surplus);
        }
        else {
            return hasPathSum(root.left,surplus)||hasPathSum(root.right,surplus);
        }
    }

    //二叉树的所有路径
    public List<String> binaryTreePaths(TreeNode root) {
        List<String> path=new ArrayList<>();
        if (root!=null){
            String val= String.valueOf(root.val);
            List<String> left=binaryTreePaths(root.left);
            int i=left.size();
            List<String> right=binaryTreePaths(root.right);
            int j=right.size();
            if (i==0&&j==0){
                path.add(val);
            }
            else {
                for (int k=0;k<i;k++){
                    String newpath=val+"->"+left.get(k);
                    path.add(newpath);
                }
                for (int k=0;k<j;k++){
                    String newpath=val+"->"+right.get(k);
                    path.add(newpath);
                }
            }

        }
        return path;
    }

    //二叉搜索树的最近公共祖先
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {

        if (p.val==root.val){
            return p;
        }
        else if (q.val==root.val){
            return q;
        }
        //分别在左右子树
        else if ((p.val<root.val&&root.val<q.val)||(q.val<root.val&&root.val<p.val)) return root;
        //都在左子树
        else if (p.val<root.val&&q.val<root.val){
            return lowestCommonAncestor(root.left,p,q);
        }
        //都在右子树
        else {
            return lowestCommonAncestor(root.right,p,q);
        }
    }

    //有序数组转换为高度平衡的搜索二叉树
    public TreeNode sortedArrayToBST(int[] nums) {
        int len=nums.length;
        if (len==0) return null;
        int r=len/2;
        TreeNode root=new TreeNode(nums[r]);
        if (r>=1){
            root.left=sortedArrayToBST(Arrays.copyOfRange(nums,0,r));
            root.right=sortedArrayToBST(Arrays.copyOfRange(nums,r+1,nums.length));
        }
        return root;

    }


    //二叉树的直径
    public int diameterOfBinaryTree(TreeNode root) {
        if (root==null) return 0;
        return Math.max(Math.max(diameterOfBinaryTree(root.left),diameterOfBinaryTree(root.right)),maxDepth(root.left)+maxDepth(root.right));
    }


    //平衡二叉树
    public static boolean isBalanced(TreeNode root) {
        if (root==null) return true;
        while (root.left!=null||root.right!=null){
            if (Math.abs(maxDepth(root.left)-maxDepth(root.right))<=1){
                return isBalanced(root.left)&&isBalanced(root.right);
            }
            else return false;
        }
        return true;
    }


    //合并二叉树
    public TreeNode mergeTrees(TreeNode t1, TreeNode t2) {
        if (t1==null&&t2!=null) return t2;
        else if (t2==null&&t1!=null) return t1;
        else if (t1==null&&t2==null) return null;
        else {
            t1.val=t1.val+t2.val;
            t1.left=mergeTrees(t1.left,t2.left);
            t1.right=mergeTrees(t1.right,t2.right);
            return t1;
        }
    }

    //左叶子之和
    public static int sumOfLeftLeaves(TreeNode root) {
        if (root==null) return 0;
        else if (root.left==null) return sumOfLeftLeaves(root.right);

            //下一层存在左叶子
        else if (root.left.left==null&&root.left.right==null) return root.left.val+sumOfLeftLeaves(root.right);
        else return sumOfLeftLeaves(root.left)+sumOfLeftLeaves(root.right);
    }
    //最小深度
    public static int minDepth(TreeNode root) {
        if(root==null) return 0;
        if(root.left==null&&root.right==null) return 1;
        else if (root.left==null) return minDepth(root.right)+1;
        else if (root.right==null) return minDepth(root.left)+1;

        else {
            return Math.min(minDepth(root.left),minDepth(root.right))+1;
        }

    }

    //最大深度
    public static int maxDepth(TreeNode root) {
        if (root==null) return 0;
        else {
            return Math.max(maxDepth(root.left),maxDepth(root.right))+1;
        }
    }

    //对称二叉树
    public static boolean isSymmetric(TreeNode root) {
        return isMirror(root,root);
    }

    public static boolean isMirror(TreeNode root1,TreeNode root2){
        if (root1==null&&root2==null) return true;
        if (root1==null^root2==null) return false;

        if (root1.val!=root2.val) return false;
        else {
            if (root1.left==null&&root1.right==null&root2.left==null&root2.right==null) return root1.val==root2.val;
            else return isMirror(root1.left,root2.right)&&isMirror(root1.right,root2.left);
        }
    }


    //翻转二叉树
    public static TreeNode invertTree(TreeNode root){
        if (root==null) return null;
        else {
            TreeNode left=root.left;
            TreeNode right=root.right;
            root.left=invertTree(right);
            root.right=invertTree(left);
        }
        return root;
    }



    //相同的树
    public static boolean isSameTree(TreeNode p, TreeNode q) {
        if (p==null||q==null) return p==null&&q==null;
        if (p.val!=q.val)return false;
        else {
            if (p.left!=null^q.left!=null) return false;
            else if (p.right!=null^q.right!=null) return false;
            else if (p.left==null&&q.right==null&&q.left==null&&q.right==null)return p.val==q.val;
            else {
                return isSameTree(p.left,q.left)&&isSameTree(p.right,q.right);
            }
        }
    }



    //两个数组的交集
    public static int[] intersection(int[] nums1, int[] nums2) {
        List<Integer> list1=transInt(nums1);
        List<Integer> list2=transInt(nums2);
        ListIterator iterator1=list1.listIterator();
        ListIterator iterator2=list2.listIterator();
        List<Integer> num=new ArrayList<>();
        while (iterator1.hasNext()){
            if (!iterator2.hasNext()) break;
            int a= (int) iterator1.next();
            int b= (int) iterator2.next();
            if (a<b) iterator2.previous();
            else if (a==b) {
                if (!num.contains(a))num.add(a);
            }
            else iterator1.previous();
        }
        int[] re=new int[num.size()];
        for (int i=0;i<num.size();i++){
            re[i]=num.get(i);
        }
        return re;
    }

    public static List<Integer> transInt(int[] nums){
        List<Integer> list1=new ArrayList<>();
        for (int i:nums){
            list1.add(i);
        }
        Collections.sort(list1);
        return list1;
    }

    //字母易位词
    public static boolean isAnagram(String s, String t) {
        List<Integer> list1=transString(s);
        List<Integer> list2=transString(t);
        Iterator iterator1=list1.iterator();
        Iterator iterator2=list2.iterator();
        while (iterator1.hasNext()){
            if (!iterator2.hasNext()) return false;
            else if (iterator1.next()!=iterator2.next()) return false;
        }
        if (iterator2.hasNext()) return false;
        return true;
    }

    public static List<Integer> transString(String s){
        List<Integer> list=new ArrayList<>();
        int len=s.length();
        for (int i=0;i<len;i++){
            char a=s.charAt(i);
            list.add((int)a);
        }
        Collections.sort(list);
        return list;
    }


    //模拟行走机器人
    public static int robotSim(int[] commands, int[][] obstacles) {
        //将障碍点放在set中
        Set<Long> set = new HashSet();
        for (int[] obstacle: obstacles) {
            long ox = (long) obstacle[0] + 30000;
            long oy = (long) obstacle[1] + 30000;
            set.add((ox << 16) + oy);
        }
        int x=0;
        int y=0;
        boolean di=false;//方向，true为x方向，false 为y方向
        int ca=1;//1为正向，-1为逆向
        int[] code={0,0};
        for (int i:commands){
            //左转
            if (i==-2){
                //切换方向
                di=!di;
                if (!di) ca=-ca;
            }
            //右转
            else if (i==-1){
                //切换方向
                di=!di;
                if (di) ca=-ca;
            }
            //前进
            else {
                for (int j=i;j>0;j--){
                    int ox=code[0];
                    int oy=code[1];
                    if (di){
                        code[0]=code[0]+ca;
                    }
                    else {
                        code[1]=code[1]+ca;
                    }
                    long s = (((long) code[0] + 30000) << 16) + ((long) code[1] + 30000);
                    if (set.contains(s)){
                        code[0]=ox;
                        code[1]=oy;
                        break;
                    }
                }
            }
        }
        int ans=code[0]*code[0]+code[1]*code[1];
        return ans;
    }

    //柠檬水找零
    public static boolean lemonadeChange(int[] bills) {
        int five=0;
        int ten=0;
        for (int i:bills){
            if (i==5) five++;
            else if (i==10) {
                if (five==0) return false;
                else {
                    five--;
                    ten++;
                }
            }
            else {
                if (ten==0){
                    if (five<3) return false;
                    else five=five-3;
                }
                else {
                    ten--;
                    if (five==0)return false;
                    else five--;
                }
            }
        }
        return true;
    }

    //分发饼干
    public static int findContentChildren(int[] g, int[] s) {
        int k=0;
        PriorityQueue<Integer> p1=new PriorityQueue<>();
        PriorityQueue<Integer> p2=new PriorityQueue<>();
        //g，s入堆
        for(int i:g){
            p1.add(i);
        }
        for (int i:s){
            p2.add(i);
        }
        while (p2.size()!=0&&p1.size()!=0){
            int g1=p1.peek();
            int s1=p2.peek();
            if (g1<=s1){
                p1.poll();
                p2.poll();
                k++;
            }
            else {
                p2.poll();
            }
        }
        return k;
    }


    //找到数据流中第K大元素的类
    static class KthLargest {

        private PriorityQueue<Integer> priorityQueue=new PriorityQueue<>();
        int size;

        public KthLargest(int k, int[] nums) {
            size=k;
            for (int i:nums){
                priorityQueue.add(i);
                if (priorityQueue.size()>k) priorityQueue.poll();
            }
        }

        public int add(int val) {
            priorityQueue.add(val);
            if (priorityQueue.size()>size) priorityQueue.poll();
            return priorityQueue.peek();
        }
    }


    //比较含退格的字符串
    public static boolean backspaceCompare(String S, String T) {
        Stack s=makeStack(S);
        Stack t=makeStack(T);
        while (!s.empty()){
            if (t.empty()||s.pop()!=t.pop()) return false;
        }
        return true;
    }

    public static Stack makeStack(String s){
        Stack stack=new Stack();
        for (int i=0;i<s.length();i++){
            char a=s.charAt(i);
            if (a!='#'){
                stack.push(a);
            }
            else if (a=='#'&&!(stack.empty())) stack.pop();
        }
        return stack;
    }

    //棒球比赛
    public static int calPoints(String[] ops) {
        int score=0;
        Stack stack=new Stack();
        for (int i=0;i<ops.length;i++){
            switch (ops[i]){
                case "+":
                    int a1= (int) stack.pop();
                    int b1= (int) stack.pop();
                    int c1=a1+b1;
                    stack.push(b1);
                    stack.push(a1);
                    stack.push(c1);
                    break;
                case "D":
                    int a2= (int) stack.peek();
                    int b2=a2*2;
                    stack.push(b2);
                    break;
                case "C":
                    stack.pop();
                    break;
                default:
                    stack.push(Integer.parseInt(ops[i]));

            }
        }
        while (!stack.empty()){
            score=score+(int)stack.pop();
        }
        return score;
    }

    //下一个比他大的数
    public static int[] nextGreaterElement(int[] nums1, int[] nums2) {
        int[] array=new int[nums1.length];

        for (int i=0;i<nums1.length;i++){
            int a=nums1[i];
            //nums2入栈
            Stack stack=new Stack();
            for (int j=0;j<nums2.length;j++) stack.push(nums2[j]);
            int x=-1;
            int b= (int) stack.pop();
            while (a!=b){
                if (a<b) x=b;
                b= (int) stack.pop();
            }
            array[i]=x;
        }
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
