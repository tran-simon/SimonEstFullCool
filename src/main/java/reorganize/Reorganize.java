package reorganize;

import org.json.JSONArray;
import org.json.JSONObject;
import sqlpush.Main;

import java.io.File;

public class Reorganize extends JSONArray{

    public Reorganize(String filePath){
        super(Main.readFile(filePath));
    }

    public Article getArticle(int i){
        return new Article(getJSONObject(i));
    }



    public static void main(String[] args) {
        String fileName = "3000";
        Reorganize reorganize = new Reorganize(fileName + ".json");


        String finalString = "";
        for(int i = 0; i < reorganize.length(); i++){
            Article article = reorganize.getArticle(i);
            String string = article.format();
            if (string != null) {
                string = string + "\n";
                finalString += string;
                System.out.println(string);

            }
        }

        Main.writeToFile(fileName +  "_ClickMin:" + reorganize.getArticle(0).getClickMin() + ".txt", finalString);
    }

    public class Article {

        private JSONObject object;
        private int clickMin = 0;

        public Article(JSONObject jsonObject) {
            object = jsonObject;
        }



        public String format(){
            int clickCount = this.getClick();
            String keywords = this.getKeywords();
            if (keywords.equals("")) {
                return null;
            }
            keywords = keywords.toLowerCase();
            keywords = keywords.replaceAll(",", " ");
            keywords = keywords.replaceAll("[^a-zéêëèöôòâàäïîìçûüùœ -]", "");

            if(clickCount <= clickMin){
                return null;
            }
            return "__label__" + clickCount + " " + keywords;

        }

        public int getClick(){
            return Integer.parseInt(object.getString("click"));
        }

        public String getKeywords(){
            return object.getString("keywords");
        }
        public JSONArray names(){
            return object.names();
        }


        public JSONObject getObject() {
            return object;
        }

        public void setObject(JSONObject object) {
            this.object = object;
        }

        public int getClickMin() {
            return clickMin;
        }

        public void setClickMin(int clickMin) {
            this.clickMin = clickMin;
        }
    }

}
