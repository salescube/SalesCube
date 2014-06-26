/**
 *
 */
package jp.co.arkinfosys.common;

/**
 * ヘルプテキストクラス
 *
 * @author Ark Information Systems
 *
 */
public class HelpText {

	public String helpText;// ヘルプテキスト
	public Boolean display = true;//ヘルプテキストを表示するか否か
	public String cursorStyle;// ヘルプテキストを表示するか否か

	public HelpText(){
		if(display){
			cursorStyle = "help";
		}
	}
}
