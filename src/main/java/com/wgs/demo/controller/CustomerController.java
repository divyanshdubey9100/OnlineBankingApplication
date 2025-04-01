package com.wgs.demo.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wgs.demo.classes.Customer;
import com.wgs.demo.classes.CustomerRegReq;
import com.wgs.demo.classes.Passbook;
import com.wgs.demo.impl.CustRequestService;
import com.wgs.demo.impl.MethodService;
import com.wgs.demo.repo.CustRegReqRepo;
import com.wgs.demo.repo.CustRepo;
import com.wgs.demo.repo.PassbookRepo;

@Controller
public class CustomerController {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired
	CustRepo custRepo;
	@Autowired
	MethodService impl;
	@Autowired
	CustRequestService reqService;
	@Autowired
	PassbookRepo pbookRepo;
	@Autowired
	CustRegReqRepo reqRepo;
	@Autowired
	MethodService methodService;
	@Autowired
	CustRegReqRepo custRegReqRepo;
	
	@RequestMapping("customerLogin")
	private String customerLogin() {
		return "Customer/customerLogin";
	}

	@RequestMapping("custAuth")
	private String CustomerAuth(@RequestParam String email, @RequestParam String pass, Model model, Customer customer,
			HttpSession session) {
		if (impl.customerAuthintication(email, pass) == true) {
			List<Customer> list = impl.findByEmail(email);
			for (Customer cust : list) {
				session.setAttribute("custName", cust.getName());
				session.setAttribute("custAccno", cust.getAccno());
			}
			return "redirect:/customer";
		}
		return "redirect:/customerLogin";
	}

	@RequestMapping("customer")
	private String custUi(HttpSession session, Model model) {
		Object userName = session.getAttribute("custName");
		if (session.getAttribute("custName") == null) {
			return "redirect:/customerLogin";
		}
		model.addAttribute("name", userName);
		return "Customer/customer";
	}

	@RequestMapping("viewCustomer")
	private String viewCust(HttpSession session,Model model) {
		if (session.getAttribute("custName") == null && session.getAttribute("custAccno") == null) {
			return "redirect:/customerLogin";
		}
		int accNo=(int) session.getAttribute("custAccno");
		List<Customer> list=custRepo.findByAccno(accNo);
		model.addAttribute("cust", list);
		return "Customer/custList";
	}

	@RequestMapping("checkAccountStatement")
	private String checkAccountStatement(Customer customer, Model model, HttpSession session) {
		if (session.getAttribute("custName") == null && session.getAttribute("custAccno") == null) {
			return "redirect:/customerLogin";
		}
		int accNo=(int) session.getAttribute("custAccno");
		List<Passbook> list=pbookRepo.findByAccNo(accNo);
		model.addAttribute("cust", list);
		return "Customer/custAccStmt";
	}

	@RequestMapping("customerHelp")
	public String custHelp() {
		return "Customer/custHelp";
	}

	@RequestMapping("/forgetCustPass&Mail")
	private String forgetCustUidAndPass() {
		return "Customer/forgetCustDetails";
	}

	@RequestMapping("resetCustPass")
	private String forgetPwd(@RequestParam String mobile, String email, Model model) {
		List<Customer> list = impl.findMailAndMobile(email, mobile);
		if (list.size() != 0) {
			model.addAttribute("cust", list);
			return "Customer/resetCustPass";
		} else {
			String msg = "Hi " + email + " Mobile_no " + mobile + " No Details Found !";
			model.addAttribute("cust", msg);
		}
		return "Customer/customerDetails";
	}

	@RequestMapping("resetCustMail")
	private String findCustMail(@RequestParam String name, String mobile, Model model) {
		List<Customer> list = impl.findUidAndMobile(name, mobile);
		if (list.size() != 0) {
			for (Customer cust : list) {
				model.addAttribute("cust", cust.getEmail());
			}
		} else {
			String msg = "Hi !" + name + " Mobile_no " + mobile + " No Details Found !";
			model.addAttribute("cust", msg);
		}
		return "Customer/customerDetails";
	}

	@RequestMapping("updateCustPass")
	private String updatePass(Model model, Customer customer) {
		custRepo.saveAndFlush(customer);
		model.addAttribute("cust", customer.getPass());
		return "Customer/customerDetails";
	}

	@RequestMapping("/custLogout")
	private String logout(HttpSession session) {
		session.removeAttribute("custName");
		session.removeAttribute("custAccno");
		return "redirect:/";
	}

	@RequestMapping("custBanking")
	private String banking(HttpSession session, Model model) {
		if (session.getAttribute("custName") == null && session.getAttribute("custAccno") == null) {
			return "redirect:/customerLogin";
		}
		model.addAttribute("accno", session.getAttribute("custAccno"));
		return "Customer/custBanking";
	}

	@RequestMapping("custWithdraw")
	private String withdraw(Customer customer, Model model, HttpSession session,Passbook passbook) {
		if (session.getAttribute("custName") == null && session.getAttribute("custAccno") == null) {
			return "redirect:/customerLogin";
		}
		if (impl.isAccExists(customer.getAccno()) == true) {
			List<Customer> custList = custRepo.findByAccno(customer.getAccno());
			for (Customer cust : custList) {
				if ((cust.getBalance() - customer.getBalance()) >= 1000 && cust.getBalance() > customer.getBalance()) {
					String timeStamp = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss").format(Calendar.getInstance().getTime());
					String trxId=impl.trxIdGen(customer.getAccno());
					passbook.setTrxId(trxId);
					passbook.setCustName(cust.getName());
					passbook.setAccNo(cust.getAccno());
					passbook.setAmtBefTrx(cust.getBalance());
					passbook.setTrxAmt(customer.getBalance());
					int newAmount = cust.getBalance() - customer.getBalance();
					passbook.setCurrentBalance(newAmount);
					passbook.setTrxDate(timeStamp);
					passbook.setTrxMode("Debit");
					cust.setBalance(newAmount);
					Passbook pass=pbookRepo.saveAndFlush(passbook);
					logger.info("Value of Passook "+pass);
					String msg = "Hi : " + cust.getName() + " : " + customer.getBalance()
							+ " is Successfully Withdrawn in a/c : " + cust.getAccno() + " Updated Balance is : "
							+ cust.getBalance();
					model.addAttribute("cust", msg);
					custRepo.flush();
				} else {
					String msg = "Hi : " + cust.getName() + " your a/c : " + cust.getAccno()
							+ " has Low A/c Balance To Withraw";
					model.addAttribute("cust", msg);
				}
			}
		} else {
			String msg = "Hii :" + customer.getAccno() + " Invalid A/c no.";
			model.addAttribute("cust", msg);
		}
		return "Customer/customerDetails";
	}

	@RequestMapping("custCheckBalance")
	private String checkBalance(Customer customer, Model model, HttpSession session) {
		if (session.getAttribute("custName") == null && session.getAttribute("custAccno") == null) {
			return "redirect:/customerLogin";
		}
		List<Customer> custList = custRepo.findByAccno(customer.getAccno());
		for (Customer cust : custList) {
			String bal = "Hello " + cust.getName() + " your a/c : " + cust.getAccno() + " balance : "
					+ cust.getBalance();
			logger.info(bal);
			model.addAttribute("cust", bal);
		}

		return "Customer/customerDetails";
	}

	@RequestMapping("custDeposit")
	private String deposit(Customer customer, Model model, HttpSession session,Passbook passbook) {
		if (session.getAttribute("custName") == null && session.getAttribute("custAccno") == null) {
			return "redirect:/customerLogin";
		}
		if (impl.isAccExists(customer.getAccno()) == true) {
			List<Customer> custList = custRepo.findByAccno(customer.getAccno());
			for (Customer cust : custList) {
				if (impl.isAccExists(customer.getAccno()) == true) {
					String timeStamp = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss").format(Calendar.getInstance().getTime());
					String trxId=impl.trxIdGen(customer.getAccno());
					passbook.setTrxId(trxId);
					passbook.setCustName(cust.getName());
					passbook.setAccNo(cust.getAccno());
					passbook.setAmtBefTrx(cust.getBalance());
					passbook.setTrxAmt(customer.getBalance());
					int newAmount = cust.getBalance() + customer.getBalance();
					passbook.setCurrentBalance(newAmount);
					passbook.setTrxDate(timeStamp);
					passbook.setTrxMode("Credit");
					cust.setBalance(newAmount);
					Passbook pass=pbookRepo.saveAndFlush(passbook);
					logger.info("Value of Passook "+pass);
					String msg = "Hi " + cust.getName() + " " + customer.getBalance()
							+ " is Successfully Deposited in A/c : " + cust.getAccno() + " Updated Balance is "
							+ cust.getBalance();
					model.addAttribute("cust", msg);
					custRepo.flush();
				}
			}
		} else {
			String msg = "Hii :" + customer.getAccno() + " Invalid A/c no.";
			model.addAttribute("cust", msg);
		}
		return "Customer/customerDetails";
	}
	
	@RequestMapping("custEdit")
	private String editCustInfo(Model model, HttpSession session) {
		if (session.getAttribute("custName") == null && session.getAttribute("custAccno") == null) {
			return "redirect:/customerLogin";
		}
		int accNo=(int) session.getAttribute("custAccno");
		List<Customer> acList = custRepo.findByAccno(accNo);
		model.addAttribute("cust", acList);
		return "Customer/editCustDetails";
	}

	@RequestMapping("editCustDetail")
	private String editCustDetail(Customer customer, Model model, HttpSession session) {
		if (session.getAttribute("custName") == null && session.getAttribute("custAccno") == null) {
			return "redirect:/customerLogin";
		}
		if (impl.isMobileExists(customer.getMobile()) == false && impl.isAccExists(customer.getAccno()) == true) {
			custRepo.saveAndFlush(customer);
			model.addAttribute("cust", customer);
			custRepo.flush();
			return "Customer/custEditList";
		} else if (impl.isAccExists(customer.getAccno()) == false) {
			String mes = customer.getAccno() + " already exists! plz Wait...";
			logger.info(mes);
			model.addAttribute("cust", mes);
		} else if (impl.isMobileExists(customer.getMobile()) == true) {
			String mes = "Try with new Mobile No.. " + customer.getMobile() + " already exists!";
			model.addAttribute("cust", mes);
		}
		return "Customer/customerDetails";
	}
	@RequestMapping("/custAccReq")
	private String custAccReqForm() {
		return "Customer/openCustomerAccount";
	}
	@RequestMapping("submitCustAccReq")
	private String submitCustomerAccReq(CustomerRegReq custReq, Model model, HttpSession session) {
		int accRefNo = 1000 + reqService.getTokenId();
		int accno=reqService.generateNewAccNo(accRefNo);
//		logger.info("Refno "+accRefNo+" accno "+accno);
		try {
			for (int i = 0; i <= reqService.getTokenId(); i++) {
				accno++;
				if (custReq.getBalance() >= 1000 && reqService.isAccExists(accno) == false
						&& methodService.isAccExists(accno) == false
						&& reqService.isMobileExists(custReq.getMobile()) == false
						&& reqService.isMailExists(custReq.getEmail()) == false) {
					custReq.setAccno(accno);
					reqRepo.save(custReq);
					model.addAttribute("cust", custReq+ "Request Submitted Successfully..");
					break;
				} else if (reqService.isAccExists(accno) == true) {
					String mes = accno + " already exists! plz Wait...";
					logger.info(mes);
					custRegReqRepo.deleteById(accno);
					reqRepo.flush();
					model.addAttribute("cust", mes);
					continue;
				} else if (reqService.isMobileExists(custReq.getMobile()) == true) {
					String mes = "Try with new Mobile No.. " + custReq.getMobile() + " already exists!";
					model.addAttribute("cust", mes);
					break;
				} else if (impl.isMailExists(custReq.getEmail()) == true) {
					String mes = "Try with new Mobile No.. " + custReq.getEmail() + " already exists!";
					model.addAttribute("cust", mes);
					break;
				}
			}
		} catch (Exception e) {
			logger.info(e + " err hai err");
		}
		return "Customer/customerDetails";
	}
}