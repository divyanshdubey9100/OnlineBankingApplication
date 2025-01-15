package com.wgs.demo.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wgs.demo.classes.Admin;
import com.wgs.demo.classes.Customer;
import com.wgs.demo.classes.Owner;
import com.wgs.demo.classes.Passbook;
import com.wgs.demo.impl.AdminImpl;
import com.wgs.demo.impl.AdminReqImpl;
import com.wgs.demo.impl.CustReqImpl;
import com.wgs.demo.impl.MethodImpl;
import com.wgs.demo.impl.OwnerImpl;
import com.wgs.demo.repo.AdminRepo;
import com.wgs.demo.repo.AdminRequestRepo;
import com.wgs.demo.repo.AdminUpdateRepo;
import com.wgs.demo.repo.CustRegReqRepo;
import com.wgs.demo.repo.CustRepo;
import com.wgs.demo.repo.OwnerRepo;
import com.wgs.demo.repo.PassbookRepo;

@Controller
public class OwnerController {
	@Autowired
	OwnerImpl ownerImpl;
	@Autowired
	MethodImpl impl;
	@Autowired
	AdminImpl adminImpl;
	@Autowired
	CustReqImpl custReqImpl;
	@Autowired
	CustRepo custRepo;
	@Autowired
	PassbookRepo pbookRepo;
	@Autowired
	AdminRepo adminRepo;
	@Autowired
	OwnerRepo ownerRepo;
	@Autowired
	AdminReqImpl reqImpl;
	@Autowired
	AdminRequestRepo reqRepo;
	@Autowired
	CustRegReqRepo custRegReqRepo;
	@Autowired
	AdminUpdateRepo adminUpdateRepo;

	@RequestMapping("regOwn")
	private String createOwnAcc() {
		return "Owner/openAccount";
	}

	@RequestMapping("regOwnAccount")
	private String registerOwner(Owner owner, Model model) {
		if (ownerImpl.isUserIdExists(owner.getUserId()) == false && ownerImpl.isMobileExists(owner.getMobile()) == false
				&& ownerImpl.getTokenId() == 0) {
			Owner own = ownerRepo.save(owner);
			String mes = own + " created Successfully!";
			model.addAttribute("cust", mes);
		} else if (ownerImpl.isUserIdExists(owner.getUserId()) == true) {
			String mes = owner.getUserId() + " Already Exists";
			model.addAttribute("cust", mes);
		} else if (ownerImpl.isMobileExists(owner.getMobile()) == true) {
			String mes = owner.getMobile() + " Already Exists";
			model.addAttribute("cust", mes);
		} else if (ownerImpl.getTokenId() != 0) {
			String mes = ownerImpl.getTokenId() + " Users Already Exists";
			model.addAttribute("cust", mes);
		}
		return "Owner/ownerDetails";
	}
	
	@RequestMapping("ownLogin")
	private String ownerLogin() {
		return "Owner/login";
	}

	@RequestMapping("ownAuth")
	private String ownerAuthentication(@RequestParam String userId, @RequestParam String pass, Model model,
			HttpSession session) {
		if (ownerImpl.ownerAuthintication(userId, pass) == true) {
			List<Owner> list = ownerImpl.findByuId(userId);
			for (Owner own : list) {
				session.setAttribute("ownName", own.getName());
				session.setAttribute("ownId", own.getId());
				session.setAttribute("ownUserId", own.getUserId());
			}
			return "Owner/ownQuesVerification";
		} else {
			return "redirect:/ownLogin";
		}
	}

	@RequestMapping("ownVerificationViaQues")
	private String ownLoginVerify(Owner owner, HttpSession session, Model model) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		List<Owner> list=ownerImpl.findByuId(session.getAttribute("ownUserId").toString());
		if (list.size() != 0) {
			System.out.println("Record Found " + list.size());
			System.out.println(list);
			for (Owner own : list) {
				if (owner.getQues1().equals(own.getQues1()) && owner.getAns1().equals(own.getAns1())
						&& owner.getQues2().equals(own.getQues2()) && owner.getAns2().equals(own.getAns2())) {
					System.out.println("Question Verification Successful..");
					return "redirect:/own";
				} else {
					String msg = "Hi Enter Valid Ques and Ans";
					System.out.println(owner);
					model.addAttribute("cust", msg);
				}
			}
		} else {
			String msg = "Hi !" + owner.getName() + " Mobile_no " + owner.getMobile() + " No Details Found !";
			model.addAttribute("cust", msg);
		}
		return "Owner/ownerDetails";
	}


	@RequestMapping("own")
	private String ownerUi(Model model, HttpSession session) {
//		Object userName = session.getAttribute("ownName");
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		model.addAttribute("name", session.getAttribute("ownName"));
		return "Owner/owner";
	}

	@RequestMapping("/ownLogout")
	private String ownerLogout(HttpSession session) {
		session.removeAttribute("ownName");
		session.removeAttribute("ownId");
		session.removeAttribute("ownUserId");
		return "redirect:/" + "ownLogin";
	}

	@RequestMapping("viewOwnProfile")
	private String viewOwnInfo(Model model, HttpSession session) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		int id = (int) session.getAttribute("ownId");
		model.addAttribute("cust", ownerImpl.getDetaislById(id));
		return "Owner/editProfileDetails";
	}

	@RequestMapping("editOwn")
	private String editOwner(@RequestParam int id, Model model, HttpSession session) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		model.addAttribute("cust", ownerImpl.getDetaislById(id));
		return "Owner/editOwnDetails";
	}

	@RequestMapping("updateOwnProfile")
	private String editAdminDetail(Owner owner, Model model, HttpSession session) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		if (ownerImpl.isMobileExists(owner.getMobile()) == false
				&& ownerImpl.isUserIdExists(owner.getUserId()) == false) {
			ownerRepo.saveAndFlush(owner);
			model.addAttribute("cust", owner);
			ownerRepo.flush();
			return "Owner/ownerDetails";
		} else if (ownerImpl.isUserIdExists(owner.getUserId()) == true) {
			String mes = owner.getUserId() + " not avail! plz Wait...";
			System.out.println(mes);
			model.addAttribute("cust", mes);
		} else if (ownerImpl.isMobileExists(owner.getMobile()) == true) {
			String mes = "Try with new Mobile No.. " + owner.getMobile() + " already exists!";
			model.addAttribute("cust", mes);
		}
		return "Owner/ownerDetails";
	}

	@RequestMapping("ownerHelp")
	public String ownHelp() {
		return "Owner/ownHelp";
	}

	@RequestMapping("/forgetOwnPass&Mail")
	private String forgetOwnUidAndPass() {
		return "Owner/forgetOwnDetails";
	}

	@RequestMapping("resetOwnPass")
	private String forgetPwd(Owner owner, Model model) {
		List<Owner> list = ownerImpl.findMailAndMobile(owner.getUserId(), owner.getMobile());
		if (list.size() != 0) {
			System.out.println("Record Found " + list.size());
			for (Owner own : list) {
				if (owner.getQues1().equals(own.getQues1()) && owner.getAns1().equals(own.getAns1())
						&& owner.getQues2().equals(own.getQues2()) && owner.getAns2().equals(own.getAns2())) {
					System.out.println("Question Verification Successful..");
					model.addAttribute("cust", list);
					return "Owner/resetOwnPass";
				} else {
					String msg = "Hi Enter Valid Ques and Ans";
					model.addAttribute("cust", msg);
				}
			}
		} else {
			String msg = "Hi " + owner.getUserId() + " Mobile_no " + owner.getMobile() + " No Details Found !";
			model.addAttribute("cust", msg);
		}
		return "Owner/ownerDetails";
	}

	@RequestMapping("resetOwnMail")
	private String findOwnMail(Owner owner, Model model) {
		List<Owner> list = ownerImpl.findUidAndMobile(owner.getName(), owner.getMobile());
		if (list.size() != 0) {
			System.out.println("Record Found " + list.size());
			for (Owner own : list) {
				if (owner.getQues1().equals(own.getQues1()) && owner.getAns1().equals(own.getAns1())
						&& owner.getQues2().equals(own.getQues2()) && owner.getAns2().equals(own.getAns2())) {
					System.out.println("Question Verification Successful..");
					model.addAttribute("cust", own.getUserId());
				} else {
					String msg = "Hi Enter Valid Ques and Ans";
					System.out.println(owner);
					model.addAttribute("cust", msg);
				}
			}
		} else {
			String msg = "Hi !" + owner.getName() + " Mobile_no " + owner.getMobile() + " No Details Found !";
			model.addAttribute("cust", msg);
		}
		return "Owner/ownerDetails";
	}

	@RequestMapping("updateOwnPass")
	private String updatePass(Model model, @RequestParam int id,String pass) {
		List<Owner> list = ownerRepo.findById(id);
		if (list.size() != 0) {
			for (Owner li : list) {
				if (li.getPass().equals(pass)) {
					String mes = "password can't be same as previouse one : " + pass;
					System.out.println(mes);
					model.addAttribute("cust", mes);
				} else {
					Owner own = ownerRepo.findById(id).get(0);
					own.setPass(pass);
					ownerRepo.saveAndFlush(own);
					String mes = "Password Successfully Updated for User : "+li.getUserId();
					System.out.println(mes);
					model.addAttribute("cust", mes);
				}
			}
		}
		return "Owner/ownerDetails";
	}

	@RequestMapping("findCustDetails")
	private String findCust(HttpSession session) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		return "Owner/findCustomerDetails";
	}

	@RequestMapping("showAllCust")
	private String showAllCust(Model model, HttpSession session) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		if (impl.getTokenId() != 0) {
			List<Customer> custList = custRepo.findAll();
			model.addAttribute("cust", custList);
			return "Owner/customerList";
		} else {
			String msg = "Hii : Empty Response";
			model.addAttribute("cust", msg);
		}
		return "Owner/ownerDetails";
	}

	@RequestMapping("ownCustEdit")
	private String editCustInfo(@RequestParam int accno, Model model, HttpSession session) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		List<Customer> acList = custRepo.findByAccno(accno);
		model.addAttribute("cust", acList);
		return "Owner/editCustomerDetails";
	}

	@RequestMapping("updateCustEdit")
	private String editCustDetail(Customer customer, Model model, HttpSession session) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		System.out.println(customer);
		if (impl.isMobileExists(customer.getMobile()) == false && impl.isAccExists(customer.getAccno()) == true) {
			custRepo.saveAndFlush(customer);
			model.addAttribute("cust", customer);
			custRepo.flush();
			return "Owner/customerEditList";
		} else if (impl.isAccExists(customer.getAccno()) == false) {
			String mes = customer.getAccno() + " already exists! plz Wait...";
			System.out.println(mes);
			model.addAttribute("cust", mes);
		} else if (impl.isMobileExists(customer.getMobile()) == true) {
			String mes = "Try with new Mobile No.. " + customer.getMobile() + " already exists!";
			model.addAttribute("cust", mes);
		}
		return "Owner/ownerDetails";
	}

	@RequestMapping("ownCustDelete")
	private String deleteCustInfo(@RequestParam int accno, Model model, HttpSession session) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		String mes = accno + " is Deleted Successfully";
		custRepo.deleteById(accno);
		custRepo.flush();
		System.out.println(mes);
		return "redirect:/showAllCust";
	}

	@RequestMapping("custByAccno")
	private String custDetailsBasedOnAccNO(Model model, Customer customer, HttpSession session) {
		if (impl.isAccExists(customer.getAccno()) == true) {
			if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
					|| session.getAttribute("ownUserId") == null) {
				return "redirect:/ownLogin";
			}
			List<Customer> custList = custRepo.findByAccno(customer.getAccno());
			model.addAttribute("cust", custList);
			return "Owner/customerEditList";
		} else {
			String msg = "Hii : Invalid A/c no.";
			model.addAttribute("cust", msg);
		}
		return "Owner/ownerDetails";
	}

	@RequestMapping("custByName")
	private String custByName(Model model, Customer customer, HttpSession session) {
		if (session.getAttribute("ownName") == null) {
			return "redirect:/ownLogin";
		}
		if (impl.isPersonExists(customer.getName()) == true) {
			List<Customer> custList = custRepo.findByName(customer.getName());
			model.addAttribute("cust", custList);
			return "Owner/customerEditList";
		} else {
			String msg = "Hii : No Person Exists!.";
			model.addAttribute("cust", msg);
		}
		return "Owner/ownerDetails";
	}

	@RequestMapping("custByMobile")
	private String custByMobile(Customer customer, Model model, HttpSession session) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		if (impl.isMobileExists(customer.getMobile()) == true) {
			List<Customer> custList = custRepo.findByMobile(customer.getMobile());
			model.addAttribute("cust", custList);
			return "Owner/customerEditList";
		} else {
			String msg = "Hii : Invalid Mobile No.";
			model.addAttribute("cust", msg);
		}
		return "Owner/ownerDetails";
	}

	@RequestMapping("custByEmail")
	private String custByEmail(Customer customer, Model model, HttpSession session) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		if (impl.isMailExists(customer.getEmail()) == true) {
			List<Customer> custList = custRepo.findByEmail(customer.getEmail());
			model.addAttribute("cust", custList);
			return "Owner/customerEditList";
		} else {
			String msg = "Hii : Invalid Mail_id.";
			model.addAttribute("cust", msg);
		}
		return "Owner/ownerDetails";
	}

	@RequestMapping("ownBanking")
	private String customerBanking(HttpSession session) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		return "Owner/custBanking";
	}

	@RequestMapping("custOperateionsByOwn")
	private String custOperations() {
		return "Owner/custOperations";
	}

	@RequestMapping("ownDeposit")
	private String ownDeposit(Customer customer, Model model, HttpSession session, Passbook passbook) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		if (impl.isAccExists(customer.getAccno()) == true) {
			List<Customer> custList = custRepo.findByAccno(customer.getAccno());
			for (Customer cust : custList) {
				if (impl.isAccExists(customer.getAccno()) == true) {
					String timeStamp = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss")
							.format(Calendar.getInstance().getTime());
					String trxId = impl.trxIdGen(customer.getAccno());
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
					System.out.println("Value of Passook "+pass);
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
		return "Owner/ownerDetails";
	}

	@RequestMapping("ownWithdraw")
	private String withdraw(Customer customer, Model model, HttpSession session, Passbook passbook) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		if (impl.isAccExists(customer.getAccno()) == true) {
			List<Customer> custList = custRepo.findByAccno(customer.getAccno());
			for (Customer cust : custList) {
				if ((cust.getBalance() - customer.getBalance()) > 1000 && cust.getBalance() > customer.getBalance()) {
					String timeStamp = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss")
							.format(Calendar.getInstance().getTime());
					String trxId = impl.trxIdGen(customer.getAccno());
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
					System.out.println("Value of Passook "+pass);
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
		return "Owner/ownerDetails";
	}

	@RequestMapping("ownCheckBalance")
	private String checkCustomerBalance(Customer customer, Model model, HttpSession session) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		List<Customer> custList = custRepo.findByAccno(customer.getAccno());
		for (Customer cust : custList) {
			String bal = "Hello " + cust.getName() + " your a/c : " + cust.getAccno() + " balance : "
					+ cust.getBalance();
			System.out.println(bal);
			model.addAttribute("cust", bal);
		}

		return "Owner/ownerDetails";
	}

	@RequestMapping("deleteCustAccByAccno")
	private String deleteCustomerAccByAccno(Customer customer, Model model, HttpSession session) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		if (impl.isAccExists(customer.getAccno()) == true) {
			List<Customer> custList = custRepo.findByAccno(customer.getAccno());
			for (Customer cust : custList) {
				String mes = "Hello " + cust.getName() + " your a/c " + cust.getAccno() + " balance is:"
						+ cust.getBalance() + " is Deleted Successfully";
				custRepo.deleteById(customer.getAccno());
				model.addAttribute("cust", mes);
			}
		} else {
			String msg = "Hii : Invalid A/c no.";
			model.addAttribute("cust", msg);
		}
		return "Owner/ownerDetails";
	}

	@RequestMapping("ownPassbook")
	private String checkOwnPassbook(Customer customer, Model model, HttpSession session) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		List<Passbook> list = pbookRepo.findByAccNo(customer.getAccno());
		model.addAttribute("cust", list);
		return "Owner/customerPassbook";
	}

	@RequestMapping("findAdminDetailByOwn")
	private String findAdminDetailByOwn(HttpSession session) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		return "Owner/findAdminDetail";
	}

	@RequestMapping("showAllAdminByOwn")
	private String showAllAdminByOwn(Model model, HttpSession session) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		if (adminImpl.getTokenId() != 0) {
			List<Admin> adList = adminRepo.findAll();
			model.addAttribute("cust", adList);
			return "Owner/adminList";
		} else {
			String msg = "Hii : Empty Response";
			model.addAttribute("cust", msg);
		}
		return "Owner/ownerDetails";
	}

	@RequestMapping("deleteAdminByOwn")
	private String deleteAdminInfoByOwn(@RequestParam int id, Model model, HttpSession session) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		adminRepo.deleteById(id);
		adminRepo.flush();
		String mes = id + " is Deleted Successfully";
		System.out.println(mes);
		model.addAttribute("cust", mes);
		return "redirect:/showAllAdminByOwn";
	}

	@RequestMapping("editAdminByOwn")
	private String editAdminInfoByOwner(@RequestParam int id, Model model, HttpSession session) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		List<Admin> acList = adminRepo.findById(id);
		model.addAttribute("cust", acList);
		return "Owner/editAdminDetails";
	}

	@RequestMapping("editAdminDetailByOwn")
	private String editAdminDetailByOwn(Admin admin, Model model, HttpSession session) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		System.out.println(admin);
		if (impl.isMobileExists(admin.getMobile()) == false && adminImpl.isUserIdExists(admin.getUserId()) == true) {
			adminRepo.saveAndFlush(admin);
			model.addAttribute("cust", admin);
			custRepo.flush();
			return "Owner/adminList";
		} else if (adminImpl.isUserIdExists(admin.getUserId()) == false) {
			String mes = admin.getUserId() + " not avail! plz Wait...";
			System.out.println(mes);
			model.addAttribute("cust", mes);
		} else if (adminImpl.isMobileExists(admin.getMobile()) == true) {
			String mes = "Try with new Mobile No.. " + admin.getMobile() + " already exists!";
			model.addAttribute("cust", mes);
		}
		return "Owner/ownerDetails";
	}

	@RequestMapping("openCustAccount")
	private String openCustAccount(HttpSession session) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		return "Owner/openCustomerAccount";
	}

	@RequestMapping("customerAccountDetailsByOwn")
	private String customerAccountDetailsByOwn(Customer customer, Model model, HttpSession session) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		int accRefNo = 1000 + impl.getTokenId();
		int accno = custReqImpl.generateNewAccNo(accRefNo);
//		System.out.println("Refno "+accRefNo+" accno "+accno);
		try {
			for (int i = 0; i <= impl.getTokenId(); i++) {
				accno++;
				if (customer.getBalance() >= 1000 && impl.isAccExists(accno) == false
						&& impl.isMobileExists(customer.getMobile()) == false
						&& impl.isMailExists(customer.getEmail()) == false) {
					customer.setAccno(accno);
					custRepo.save(customer);
					model.addAttribute("cust", "Account Created Successfully.." + customer);
					break;
				} else if (impl.isAccExists(accno) == true) {
					String mes = accno + " already exists! plz Wait...";
//					System.out.println(mes);
					custRegReqRepo.deleteById(accno);
					reqRepo.flush();
					model.addAttribute("cust", mes);
					continue;
				} else if (impl.isMobileExists(customer.getMobile()) == true) {
					String mes = "Try with new Mobile No.. " + customer.getMobile() + " already exists!";
					model.addAttribute("cust", mes);
					break;
				} else if (impl.isMailExists(customer.getEmail()) == true) {
					String mes = "Try with new Mobile No.. " + customer.getEmail() + " already exists!";
					model.addAttribute("cust", mes);
					break;
				}
			}
		} catch (Exception e) {
			System.out.println(e + " err hai err");
		}
		return "Owner/ownerDetails";
	}

	@RequestMapping("chkAdminReq")
	private String checkAdminReq(Model model, HttpSession session) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		model.addAttribute("cust", reqImpl.findAllReq());
		return "Owner/showAdminReq";
	}

	@RequestMapping("chkAdminUpdateReq")
	private String updateReqByAdmin(Model model, HttpSession session) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		model.addAttribute("cust", reqImpl.findUpdateReq());
		return "Owner/adminUpdateReq";
	}

	@RequestMapping("cnfrmAdminUpdateReq")
	private String confirmUpdateReq(Admin admin, Model model, HttpSession session) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		model.addAttribute("cust", admin);
		return "Owner/cnfrmAdminUpdate";
	}

	@RequestMapping("acceptAdminUpdateReq")
	private String acceptUpdateReq(Admin admin, Model model, HttpSession session) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		if (adminImpl.isUserIdExists(admin.getUserId()) == false
				&& adminImpl.isMobileExists(admin.getMobile()) == false) {
			Admin adList = adminRepo.saveAndFlush(admin);
			adminRepo.flush();
			adminUpdateRepo.deleteById(admin.getId());
			adminUpdateRepo.flush();
			String mes = adList + " created Successfully!";
			model.addAttribute("cust", mes);
		} else if (adminImpl.isUserIdExists(admin.getUserId()) == true
				&& adminImpl.isMobileExists(admin.getMobile()) == false) {
			Admin adList = adminRepo.saveAndFlush(admin);
			adminRepo.flush();
			adminUpdateRepo.deleteById(admin.getId());
			adminUpdateRepo.flush();
			String mes = adList + " created Successfully!";
			model.addAttribute("cust", mes);
		} else if (adminImpl.isMobileExists(admin.getMobile()) == true) {
			String mes = admin.getMobile() + " Already Exists" + admin.getId();
			adminUpdateRepo.deleteById(admin.getId());
			adminUpdateRepo.flush();
			model.addAttribute("cust", mes);
		}
		return "Owner/ownerDetails";
	}

	@RequestMapping("delAdminUpdateReq")
	private String deleteAdminUpdateRequest(HttpSession session, @RequestParam int id, Model model) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		adminUpdateRepo.deleteById(id);
		adminUpdateRepo.flush();
		String msg = " Admin A/c Request Denied";
		model.addAttribute("cust", msg);
		return "redirect:/chkAdminUpdateReq";
	}

	@RequestMapping("cnfrmAdminReq")
	private String confirmReq(Admin admin, Model model, HttpSession session) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		model.addAttribute("cust", admin);
		String msg = admin + "Record Deleted";
		reqRepo.deleteById(admin.getId());
		reqRepo.flush();
		System.out.println(msg);
		return "Owner/cnfrmAdminReq";
	}

	@RequestMapping("acceptAdminReq")
	private String acceptReq(Admin admin, Model model, HttpSession session) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		if (adminImpl.isUserIdExists(admin.getUserId()) == false
				&& adminImpl.isMobileExists(admin.getMobile()) == false) {
			Admin adList = adminRepo.save(admin);
			adminRepo.flush();
			String mes = adList + " created Successfully!";
			model.addAttribute("cust", mes);
		} else if (adminImpl.isUserIdExists(admin.getUserId()) == true) {
			String mes = admin.getUserId() + " Already Exists";
			reqRepo.deleteByMobile(admin.getMobile());
			reqRepo.flush();
			model.addAttribute("cust", mes);
		} else if (adminImpl.isMobileExists(admin.getMobile()) == true) {
			String mes = admin.getMobile() + " Already Exists";
			reqRepo.deleteByMobile(admin.getMobile());
			reqRepo.flush();
			model.addAttribute("cust", mes);
		}
		return "Owner/ownerDetails";
	}

	@RequestMapping("delAdminReq")
	private String deleteAdminRequest(HttpSession session, @RequestParam int id, Model model) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		reqRepo.deleteById(id);
		reqRepo.flush();
		String msg = " Admin A/c Request Denied";
		model.addAttribute("cust", msg);
		return "redirect:/chkAdminReq";
	}

	@RequestMapping("chkCustReq")
	private String checkCustReq(Model model, HttpSession session) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		model.addAttribute("cust", custReqImpl.findAllReq());
		return "Owner/showCustReq";
	}

	@RequestMapping("acceptCustReq")
	private String acceptCustomerRequest(Customer customer, Model model, HttpSession session) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		try {
			if (customer.getBalance() >= 1000 && impl.isAccExists(customer.getAccno()) == false
					&& impl.isMobileExists(customer.getMobile()) == false
					&& impl.isMailExists(customer.getEmail()) == false) {
				custRepo.save(customer);
				custRegReqRepo.deleteById(customer.getAccno());
				reqRepo.flush();
				model.addAttribute("cust", "Account Created Successfully.." + customer);
			} else if (impl.isAccExists(customer.getAccno()) == true) {
				String mes = customer.getAccno() + " already exists! plz Wait...";
				System.out.println(mes);
				model.addAttribute("cust", mes);
			} else if (impl.isMobileExists(customer.getMobile()) == true) {
				String mes = "Try with new Mobile No.. " + customer.getMobile() + " already exists!";
				model.addAttribute("cust", mes);
			} else if (impl.isMailExists(customer.getEmail()) == true) {
				String mes = "Try with new Mobile No.. " + customer.getEmail() + " already exists!";
				model.addAttribute("cust", mes);
			}
		} catch (Exception e) {
			System.out.println(e + " err hai err");
		}
		return "Owner/ownerDetails";
	}

	@RequestMapping("delCustReq")
	private String deleteCustomerRequest(HttpSession session, Customer customer, Model model) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null
				|| session.getAttribute("ownUserId") == null) {
			return "redirect:/ownLogin";
		}
		custRegReqRepo.deleteById(customer.getAccno());
		reqRepo.flush();
		String msg = " Customer A/c Request Denied";
		model.addAttribute("cust", msg);
		return "redirect:/chkCustReq";
	}
}
