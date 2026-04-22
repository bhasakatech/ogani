"use strict";

(function ($, document) {
	function resolveEndpoint(url) {
		if (!url) {
			return window.location.origin + "/bin/quote";
		}

		if (/^https?:\/\//i.test(url)) {
			return url;
		}

		if (url.charAt(0) === "/") {
			return window.location.origin + url;
		}

		var basePath = window.location.pathname;
		if (basePath.lastIndexOf("/") !== -1) {
			basePath = basePath.substring(0, basePath.lastIndexOf("/") + 1);
		}

		return window.location.origin + basePath + url;
	}

	function setStatus($tbody, className, message) {
		$tbody.empty().append(
			$("<tr></tr>").append(
				$("<td></td>")
					.attr("colspan", "4")
					.addClass(className)
					.text(message)
			)
		);
	}

	function buildRow(item) {
		var $row = $("<tr></tr>");
		var quoteText = item && item.q ? item.q : "";
		var authorText = item && item.a ? item.a : "";
		var quoteNo = item && item.c ? item.c : "";
		var quoteHtml = item && item.h ? item.h : "";

		$row.append($("<td></td>").text(quoteText));
		$row.append($("<td></td>").text(authorText));
		$row.append($("<td></td>").text(quoteNo));
		$row.append($("<td></td>").html(quoteHtml));
		return $row;
	}

	function renderQuotes($component, data) {
		var $tbody = $component.find(".quote-table__body");

		if (!Array.isArray(data) || data.length === 0) {
			setStatus($tbody, "quote-table__status", "No quotes found.");
			return;
		}

		$tbody.empty();
		data.forEach(function (item) {
			$tbody.append(buildRow(item));
		});
	}

	function parseResponse(response) {
		if (Array.isArray(response)) {
			return response;
		}

		if (typeof response === "string") {
			try {
				return JSON.parse(response);
			} catch (error) {
				return [];
			}
		}

		return [];
	}

	function initQuoteTable(component) {
		var $component = $(component);
		var $tbody = $component.find(".quote-table__body");
		var rawUrl = $component.data("quote-url") || "/bin/quote";
		var url = resolveEndpoint(rawUrl);

		setStatus($tbody, "quote-table__status", "Loading quotes...");

		$.ajax({
			url: url,
			type: "GET",
			dataType: "json",
			cache: false,
			success: function (response) {
				renderQuotes($component, parseResponse(response));
			},
			error: function () {
				setStatus($tbody, "quote-table__error", "Unable to load quotes from " + rawUrl + ".");
			}
		});
	}

	function initAllQuoteTables() {
		$(".quote-table").each(function () {
			var initialized = $(this).data("quotes-initialized");
			if (initialized) {
				return;
			}

			$(this).data("quotes-initialized", true);
			initQuoteTable(this);
		});
	}

	$(document).ready(initAllQuoteTables);
	$(document).on("foundation-contentloaded", initAllQuoteTables);
})(jQuery, document);
null