package com.gizumo.jp.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import com.gizumo.jp.repository.TodoDetail;
import com.gizumo.jp.repository.TodoDetailRepository;


@Controller
public class TodoController {
	final static Logger logger = LoggerFactory.getLogger(TodoController.class);

	@Autowired
	TodoDetailRepository todoDetailRepository;

	@Autowired
	MessageSource msg;

	/**
	 * 初期表示
	 * 
	 * @param model
	 *            モデル
	 * @return 表示リスト
	 */
	@RequestMapping(value = "/Todo", method = RequestMethod.GET)
	public String index(Model model) {
		logger.debug("Todo + index");
		List<TodoDetail> list = todoDetailRepository.findBydoFlg(false);
		if (CollectionUtils.isEmpty(list)) {
			String message = msg.getMessage("Todo.list.empty", null,
					Locale.JAPAN);
			model.addAttribute("emptyMessage", message);
		}
		model.addAttribute("list", list);
		modelDump(model, "index");
		return "Todo/index";
	}

	/**
	 * 詳細表示
	 * 
	 * @param id
	 *            表示するレコードID
	 * @return 詳細オブジェクト
	 */
	@RequestMapping(value = "/Todo/{id}", method = RequestMethod.GET)
	public ModelAndView detail(@PathVariable Integer id) {
		logger.debug("Todo + detail");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("Todo/detail");
		TodoDetail detail = todoDetailRepository.findOne(id);
		mv.addObject("todoDetail", detail);
		return mv;
	}

	/**
	 * 検索
	 * 
	 * @param keyword
	 *            検索文字列
	 * @param allFlg
	 *            実施済みも含むか否か
	 * @return 表示リスト
	 */
	@RequestMapping(value = "/Todo/search", method = RequestMethod.GET)
	public ModelAndView search(@RequestParam String keyword,
			@RequestParam(defaultValue = "false") Boolean allFlg) {
		logger.debug("Todo + search");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("Todo/index");
		List<TodoDetail> list;
		if (!StringUtils.isEmpty(keyword)) {
			if (allFlg == true) {
				list = todoDetailRepository.findTodoDetail(keyword);
			} else {
				list = todoDetailRepository.findTodoDetail(keyword, allFlg);
			}
			if (CollectionUtils.isEmpty(list)) {
				String message = msg.getMessage("Todo.list.empty", null,
						Locale.JAPAN);
				mv.addObject("emptyMessage", message);
			}
		} else {
			if (allFlg == true) {
				list = todoDetailRepository.findAll();
			} else {
				list = todoDetailRepository.findBydoFlg(allFlg);
			}
		}
		mv.addObject("list", list);
		return mv;
	}

	/**
	 * 新規作成
	 * 
	 * @param form
	 *            データ作成用Form
	 * @param model
	 *            モデル
	 * @return URL
	 */
	@RequestMapping(value = "/Todo/create", method = RequestMethod.GET)
	public String create(TodoDetailForm form, Model model) {
		logger.debug("Todo + create");
		modelDump(model, "create");
		return "Todo/create";
	}

	/**
	 * データ保存
	 * 
	 * @param form
	 *            保存用データForm
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/Todo/save", method = RequestMethod.POST)
	public String save(@Validated @ModelAttribute TodoDetailForm form,
			BindingResult result, Model model) {
		logger.debug("Todo + save");
		if (result.hasErrors()) {
			String message = msg.getMessage("Todo.validation.error", null,
					Locale.JAPAN);
			model.addAttribute("errorMessage", message);
			return create(form, model);
		}
		TodoDetail todo = convert(form);
		logger.debug("Todo:{}", todo.toString());
		todo = todoDetailRepository.saveAndFlush(todo);
		modelDump(model, "save");
		return "redirect:/Todo/" + todo.getId().toString();
	}

	/**
	 * 削除
	 * 
	 * @param id
	 *            削除対象ID
	 * @param attributes
	 * @param model
	 * @return リダイレクトURL
	 */
	@RequestMapping(value = "/Todo/delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable Integer id,
			RedirectAttributes attributes, Model model) {
		logger.debug("Todo + delete");
		TodoDetail deleteTodo = todoDetailRepository.findOne(id);
		deleteTodo.setDeleteFlg(true);
		deleteTodo.setUpdateDate(new Date());
		todoDetailRepository.saveAndFlush(deleteTodo);
		attributes.addFlashAttribute("deleteMessage", "delete ID:" + id);
		return "redirect:/Todo";
	}

	/**
	 * 更新
	 * 
	 * @param id
	 *            更新対象ID
	 * @param attributes
	 * @param model
	 * @return リダイレクトURL
	 */
	@RequestMapping(value = "/Todo/update/{id}", method = RequestMethod.GET)
	public String update(@PathVariable Integer id,
			RedirectAttributes attributes, Model model) {
		logger.debug("Todo + update");
		TodoDetail updateTodo = todoDetailRepository.findOne(id);
		updateTodo.setDoFlg(true);
		todoDetailRepository.saveAndFlush(updateTodo);
		attributes.addFlashAttribute("updateMessage", "update ID:" + id);
		return "redirect:/Todo";
	}

	/**
	 * フォームをモデルに変換
	 * 
	 * @param form
	 *            フォーム
	 * @return model
	 */
	private TodoDetail convert(TodoDetailForm form) {
		TodoDetail todo = new TodoDetail();
		todo.setTitle(form.getTitle());
		todo.setDoFlg(false);
		if (!StringUtils.isEmpty(form.getDetail())) {
			todo.setDetail(form.getDetail());
		}
		todo.setUpdateDate(new Date());
		return todo;
	}

	/**
	 * デバック用メソッド モデルの中身とアクションを確認
	 * 
	 * @param model
	 *            モデルオブジェクト
	 * @param m
	 *            アクション名
	 */
	private void modelDump(Model model, String m) {
		logger.debug(" ");
		logger.debug("Model:{}", m);
		Map<String, Object> mm = model.asMap();
		for (Entry<String, Object> entry : mm.entrySet()) {
			logger.debug("key:{}, value:{}", entry.getKey(),
					entry.getValue().toString());
		}
	}

}
