package com.example.myboot07.bean;

import com.example.myboot07.dto.QuestionDTO;
import lombok.Data;
import lombok.ToString;
import org.w3c.dom.html.HTMLParagraphElement;
import sun.awt.image.ImageWatched;

import java.util.LinkedList;
import java.util.List;

/**
 *  分页Bean
 */
@ToString
@Data
public class Page {

    // 表示当前是第几页(表示数据库上的页数)
    private Integer pageno;

    // 表示当前页面上的数量  默认是当前页面是8条数据
    private Integer pagesize;

    //  表示一共有的页数
    private Integer totalno;

    // 表示当前一共有的数量
    private Integer totalsize;

    //  用于传输数据
    private List<QuestionDTO> list = new LinkedList<QuestionDTO>();

    // 表示当前索引
    private Integer startIndex;

    // 封装在当前页面显示页码数的集合 为6个
    private List<Integer> pages = new LinkedList<Integer>();

    public void setPages(){
        this.pages.add(this.pageno);
        for(int i=1;i<=3;i++){
            if(this.pageno -i >0){
                this.pages.add(0,this.pageno-i);
            }
            if(this.pageno+i<=this.totalno){
                this.pages.add(this.pageno+i);
            }

        }

    }

    //  注意： 只要生成一个page对象 那么就应该给该对象赋值两个基本变量 1. 当前页数 2. 当前页数上上的信息最大数量
    public Page(Integer pageno,Integer pagesize){
        //  如果传进来的pageno和pagesize 小于0 那么就将设置默认值
        if(pageno<0){
            this.pageno = 0;
        }else{
            this.pageno = pageno;
        }
        if(pagesize < 0 ){
            this.pagesize  = 7;
        }else{
            this.pagesize = pagesize;
        }



    }



    public Integer getPageno() {
        return pageno;
    }

    public void setPageno(Integer pageno) {
        this.pageno = pageno;
    }

    public Integer getPagesize() {
        return pagesize;
    }

    public void setPagesize(Integer pagesize) {
        this.pagesize = pagesize;
    }

    public Integer getTotalno() {
        return totalno;
    }

    /**
     *    总的页数 不能私自设置
     * @param totalno
     */
    public void setTotalno(Integer totalno) {
        this.totalno = totalno;
    }


    public Integer getTotalsize() {
        return totalsize;
    }

    /**
     *  在设置总的数据条数的时候，应该把总的页数也设置出来
     * @param totalsize
     */
    public void setTotalsize(Integer totalsize) {
        this.totalno=(totalsize%pagesize)==0?(totalsize/pagesize):(totalsize/pagesize+1);
        this.totalsize = totalsize;
    }

    public List<QuestionDTO> getList() {
        return list;
    }

    public void setList(List<QuestionDTO> list) {
        this.list = list;
    }

    public Integer getStartIndex() {
        return (this.pageno-1)*this.pagesize;
    }







}
