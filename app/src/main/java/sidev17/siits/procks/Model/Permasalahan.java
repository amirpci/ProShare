package sidev17.siits.procks.Model;

import java.io.Serializable;

public class Permasalahan implements Serializable {
    private String pid;
    private String problem_owner;
    private String majority_id;
    private String timestamp;
    private String problem_title;
    private String problem_desc;
    private String picture_id;
    private String video_id;
    private String tag;
    private int status;
    private int totalVote;
    private int statuspost;
    private int hit;
    public Permasalahan(){

    }

    public int getHit() {
        return hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public int getStatuspost() {
        return statuspost;
    }

    public void setStatuspost(int statuspost) {
        this.statuspost = statuspost;
    }

    public int getTotalVote() {
        return totalVote;
    }

    public void setTotalVote(int totalVote) {
        this.totalVote = totalVote;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getpid() {
        return pid;
    }

    public void setpid(String pid) {
        this.pid = pid;
    }

    public String getproblem_owner() {
        return problem_owner;
    }

    public void setproblem_owner(String problem_owner) {
        this.problem_owner = problem_owner;
    }

    public String getmajority_id() {
        return majority_id;
    }

    public void setmajority_id(String majority_id) {
        this.majority_id = majority_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getproblem_title() {
        return problem_title;
    }

    public void setproblem_title(String problem_title) {
        this.problem_title = problem_title;
    }

    public String getproblem_desc() {
        return problem_desc;
    }

    public void setproblem_desc(String problem_desc) {
        this.problem_desc = problem_desc;
    }

    public String getpicture_id() {
        return picture_id;
    }

    public void setpicture_id(String picture_id) {
        this.picture_id = picture_id;
    }

    public String getvideo_id() {
        return video_id;
    }

    public void setvideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
